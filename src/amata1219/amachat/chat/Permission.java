package amata1219.amachat.chat;

import amata1219.amachat.user.User;

public interface Permission {

	boolean hasPermission(String permission);

	void addPermission(String permission);

	void removePermission(String permission);

	void clearPermissions();

	boolean hasPermissions(User user);

}
