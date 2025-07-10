(nickname);
        else {
            return PrivateMessageResponse.playerNotFound();
        }

        if (targetPlayer == null) {
            return PrivateMessageResponse.playerIsNull();
        }

        if (!targetPlayer.isOnline()) {
            return PrivateMessageResponse.playerNotOnline();
        }

        TextComponent textComponent = parseJsonToEventImpl.parsePerMessageToComponent(Tool.getPrefixComponent());
        textComponent = textComponent.append(parseJsonToEventImpl.parseMessageListToComponent(messageList));
        targetPlayer.sendMessage(textComponent);
        return PrivateMessageResponse.sendSuccess(getFoliaPlayer(targetPlayer));
    }

    @Override
    public void handleSendActionBarMessage(List<MessageSegment> messageList) {
        TextComponent actionTextComponent = parseJsonToEventImpl.parseMessageListToComponent(messageList);
        for (Player player : instance.getServer().getOnlinePlayers()) {
            if (player.isOnline()) {
                player.sendActionBar(actionTextComponent);
            }
        }
    }
}