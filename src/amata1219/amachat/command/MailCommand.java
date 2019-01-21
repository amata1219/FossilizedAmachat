package amata1219.amachat.command;

import java.util.Set;

import amata1219.amachat.mail.AbstractMail;
import amata1219.amachat.mail.Mail;
import amata1219.amachat.mail.MailManager;
import amata1219.amachat.user.User;
import amata1219.amachat.user.UserManager;
import net.md_5.bungee.api.CommandSender;

public class MailCommand extends Command {

	/*
	 * 	read - 3496342
		r - 114
		send - 3526536
		s - 115
		clear - 94746189
		c - 99
	 */

	public MailCommand(String name, String permission, String... aliases) {
		super(name, permission, aliases);
	}

	@Override
	public void complete(CommandSender sender, String[] args) {
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		User user = Command.isUser(sender);
		if(user == null)
			return;

		Arguments arguments = Arguments.newInstance(args);
		switch(arguments.getHashCodeOfArgument(0)){
		case 0:

			break;
		case 114:
		case 3496342:
			Set<AbstractMail> mails = MailManager.getMails(user.getUniqueId(), Mail.class);
			user.success(mails.size() + "件のメールが届いています。");
			for(AbstractMail mail : mails)
				mail.trySend();
			break;
		case 115:
		case 3526536:
			if(!arguments.hasArgument(1)){
				user.warn("プレイヤー名を指定して下さい。");
				break;
			}

			if(!arguments.hasArgument(2)){
				user.warn("本文を記述して下さい。");
				break;
			}

			if(!UserManager.isExist(arguments.getArgument(1))){
				user.warn("指定されたプレイヤーは存在しません。");
				break;
			}

			Mail mail = Mail.write(user.getUniqueId(), UserManager.getUniqueId(arguments.getArgument(1)), arguments.concatenateArguments(2, args.length - 1));
			MailManager.addMail(mail);
			mail.trySend();
			user.success(arguments.getArgument(1) + "さんにメールを送信しました。");
			break;
		case 99:
		case 94746189:
			Set<AbstractMail> receiveds = MailManager.getMails(user.getUniqueId(), Mail.class);
			if(receiveds.isEmpty()){
				user.warn("受信したメールは存在しません。");
				break;
			}

			int size = receiveds.size();
			for(AbstractMail received : receiveds)
				received.remove(false);

			Mail.getDatabase().apply();
			user.success(size + "件のメールを削除しました。");
			break;
		}
	}


}
