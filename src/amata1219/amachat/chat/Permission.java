package amata1219.amachat.chat;

import java.util.Set;

import amata1219.amachat.user.User;

public abstract class Permission extends Chat {

	protected Set<String> permissions;

	public boolean hasPermission(String permission){
		return permission.contains(permission);
	}

	public void addPermission(String permission){
		permissions.add(permission);
	}

	public void removePermission(String permission){
		permissions.remove(permission);
	}

	public void clearPermissions(){
		permissions.clear();
	}

	public boolean hasPermissions(User user){
		if(user == null)
			return false;

		return permissions.size() == permissions.stream().filter(permission -> user.toProxiedPlayer().hasPermission(permission)).count();
	}

}
