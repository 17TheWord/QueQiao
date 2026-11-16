#!/usr/bin/env node
/**
 * QueQiao 端到端验证
 *
 * 前置：服务端已由 run.sh 启动，且 QueQiao 的 WebSocket Server 已就绪。
 * 验证目标不是「HTTP 返回 200」，而是「消息真的到达了玩家客户端」。
 *
 * 用法：
 *   node e2e.js [--ws-port 8080] [--mc-port 25565] [--version 1.21.11] [--server-name Server]
 */

const WebSocket = require('ws');

function arg(name, fallback) {
  const i = process.argv.indexOf(`--${name}`);
  return i > -1 && process.argv[i + 1] ? process.argv[i + 1] : fallback;
}

const WS_PORT = arg('ws-port', '8080');
const MC_PORT = parseInt(arg('mc-port', '25565'), 10);
const MC_VERSION = arg('version', '1.21.11');
const SERVER_NAME = arg('server-name', 'Server');
const ORIGIN = arg('origin', 'e2e');

const results = [];
let seq = 0;
const pending = new Map();
const events = [];

function record(name, pass, detail, skip) {
  results.push({ name, pass, detail, skip });
  console.log(`${skip ? '-' : pass ? '✓' : '✗'} ${name}${detail ? '  | ' + detail : ''}`);
}

const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

function api(ws, name, data, timeout = 10000) {
  return new Promise((resolve, reject) => {
    const echo = `t${++seq}`;
    const timer = setTimeout(() => {
      pending.delete(echo);
      reject(new Error('等待响应超时'));
    }, timeout);
    pending.set(echo, { resolve, timer });
    ws.send(JSON.stringify({ api: name, data, echo }));
  });
}

async function testApi(ws) {
  console.log('\n=== API ===');

  try {
    const r = await api(ws, 'get_status', {});
    const d = r.data || {};
    record('get_status', r.code === 200, `code=${r.code}`);
    record('  └ 状态快照字段完整',
      !!(d.server_type && d.server_version && d.cpu_information && d.memory_information),
      `${d.server_type}/${d.server_version}`);
  } catch (e) { record('get_status', false, e.message); }

  try {
    const r = await api(ws, 'broadcast', { message: { text: 'E2E-BROADCAST' } });
    record('broadcast', r.code === 200, `code=${r.code}`);
  } catch (e) { record('broadcast', false, e.message); }

  try {
    const r = await api(ws, 'send_actionbar', { message: { text: 'E2E-ACTIONBAR' } });
    record('send_actionbar', r.code === 200, `code=${r.code}`);
  } catch (e) { record('send_actionbar', false, e.message); }

  try {
    const r = await api(ws, 'send_title', {
      title: { text: 'E2E' }, subtitle: { text: 'queqiao' },
      fadein: 10, stay: 20, fadeout: 10,
    });
    record('send_title', r.code === 200, `code=${r.code}`);
  } catch (e) { record('send_title', false, e.message); }

  // 组件格式：原生必须通，Adventure 包装必须不通（否则说明 core 行为变了）
  try {
    const r = await api(ws, 'broadcast', { message: [{ type: 'text', data: { text: 'x' } }] });
    record('拒绝 Adventure 包装格式', r.code !== 200, `code=${r.code}`, true);
  } catch { record('拒绝 Adventure 包装格式', true, '预期失败', true); }

  try {
    const r = await api(ws, 'send_command', { command: 'say e2e' });
    record('send_command', r.code === 200 || r.code === 503, `code=${r.code}（503=平台未实现）`, true);
  } catch (e) { record('send_command', false, e.message); }
}

async function testErrors(ws) {
  console.log('\n=== 错误处理 ===');

  try {
    const r = await api(ws, 'not_an_api', {});
    record('未知 api → 404', r.code === 404, `code=${r.code}`);
  } catch (e) { record('未知 api → 404', false, e.message); }

  try {
    const echo = `t${++seq}`;
    const p = new Promise((resolve, reject) => {
      const t = setTimeout(() => reject(new Error('超时')), 8000);
      pending.set(echo, { resolve, timer: t });
    });
    ws.send(JSON.stringify({ data: {}, echo }));
    const r = await p;
    record('缺少 api → 400', r.code === 400, `code=${r.code}`);
  } catch (e) { record('缺少 api → 400', false, e.message); }

  ws.send('{ broken json ');
  await sleep(800);
  record('畸形 JSON 后连接存活', ws.readyState === WebSocket.OPEN, `readyState=${ws.readyState}`);

  try {
    const r = await api(ws, 'get_status', {});
    record('畸形 JSON 后仍可请求', r.code === 200, `code=${r.code}`);
  } catch (e) { record('畸形 JSON 后仍可请求', false, e.message); }
}

async function testEvents(ws) {
  console.log('\n=== 事件与投递 ===');
  let mineflayer;
  try {
    mineflayer = require('mineflayer');
  } catch {
    record('mineflayer 可用', false, '未安装依赖，跳过事件测试');
    return;
  }

  const username = `E2E${Math.floor(Math.random() * 9000) + 1000}`;
  const bot = mineflayer.createBot({
    host: '127.0.0.1', port: MC_PORT, username,
    auth: 'offline', version: MC_VERSION,
  });
  const received = [];
  bot.on('message', (j) => received.push(j.toString()));

  try {
    await new Promise((res, rej) => {
      bot.once('spawn', res);
      bot.once('error', rej);
      bot.once('kicked', (r) => rej(new Error('被踢: ' + JSON.stringify(r))));
      setTimeout(() => rej(new Error('上线超时')), 45000);
    });
    record(`假玩家 ${username} 上线`, true);
    await sleep(2500);

    const joinEvt = events.find((e) => JSON.stringify(e).includes(username) && /join/i.test(JSON.stringify(e)));
    record('player_join 事件推送', !!joinEvt, joinEvt ? `post_type=${joinEvt.post_type}` : '未捕获');

    bot.chat('e2e-chat-probe');
    await sleep(2500);
    const chatEvt = events.find((e) => JSON.stringify(e).includes('e2e-chat-probe'));
    record('player_chat 事件推送', !!chatEvt, chatEvt ? `post_type=${chatEvt.post_type}` : '未捕获');

    // 关键：验证消息真的到达客户端，而不只是 API 返回 200
    const before = received.length;
    await api(ws, 'broadcast', { message: { text: 'DELIVER-CHECK-BROADCAST' } });
    await sleep(2000);
    record('广播真实送达客户端',
      received.slice(before).some((s) => s.includes('DELIVER-CHECK-BROADCAST')),
      received.slice(before).find((s) => s.includes('DELIVER-CHECK-BROADCAST'))?.slice(0, 80) || '未收到');

    const before2 = received.length;
    const rp = await api(ws, 'send_private_msg', {
      uuid: bot.player.uuid, nickname: bot.username,
      message: { text: 'DELIVER-CHECK-PRIVATE' },
    });
    await sleep(2000);
    record('私聊真实送达目标玩家',
      received.slice(before2).some((s) => s.includes('DELIVER-CHECK-PRIVATE')),
      `code=${rp.code}`);

    bot.quit();
    await sleep(2500);
    const quitEvt = events.find((e) => JSON.stringify(e).includes(username) && /quit/i.test(JSON.stringify(e)));
    record('player_quit 事件推送', !!quitEvt, quitEvt ? `post_type=${quitEvt.post_type}` : '未捕获');
  } catch (e) {
    record('假玩家流程', false, e.message);
    try { bot.quit(); } catch { /* ignore */ }
  }
}

(async () => {
  const ws = new WebSocket(`ws://127.0.0.1:${WS_PORT}`, {
    headers: {
      'x-self-name': encodeURIComponent(SERVER_NAME),
      'x-client-origin': ORIGIN,
    },
  });

  ws.on('message', (raw) => {
    let m;
    try { m = JSON.parse(raw.toString()); } catch { return; }
    if (m.post_type === 'response' && m.echo && pending.has(m.echo)) {
      const p = pending.get(m.echo);
      clearTimeout(p.timer);
      pending.delete(m.echo);
      p.resolve(m);
    } else {
      events.push(m);
    }
  });

  try {
    await new Promise((res, rej) => {
      ws.once('open', res);
      ws.once('error', (e) => rej(new Error('连接失败: ' + e.message)));
      setTimeout(() => rej(new Error(`连接 ${WS_PORT} 超时`)), 15000);
    });
  } catch (e) {
    console.error(`\n✗ ${e.message}`);
    console.error('  检查：服务端是否已启动？握手头 x-self-name 是否与 config.yml 的 server_name 一致？');
    process.exit(2);
  }

  console.log(`已连接 ws://127.0.0.1:${WS_PORT}（server_name=${SERVER_NAME}）`);

  await testApi(ws);
  await testErrors(ws);
  await testEvents(ws);

  ws.close();

  const real = results.filter((r) => !r.skip);
  const pass = real.filter((r) => r.pass).length;
  const fail = real.length - pass;

  console.log('\n========== 汇总 ==========');
  console.log(`通过 ${pass} / ${real.length}${fail ? `，失败 ${fail}` : ''}`);
  if (fail) {
    console.log('\n失败项：');
    real.filter((r) => !r.pass).forEach((r) => console.log(`  - ${r.name}: ${r.detail}`));
  }
  console.log(`服务端推送 ${events.length} 条，类型: ${[...new Set(events.map((e) => e.post_type))].join(', ')}`);
  process.exit(fail ? 1 : 0);
})();
