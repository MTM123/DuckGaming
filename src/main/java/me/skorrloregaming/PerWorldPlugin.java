package me.skorrloregaming;

import me.skorrloregaming.impl.ServerMinigame;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Warning;
import org.bukkit.Warning.WarningState;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.hanging.HangingEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.event.weather.WeatherEvent;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class PerWorldPlugin {
	public static PerWorldPlugin instance;
	public List<Class<?>> exemptEvents = Arrays.asList(new Class<?>[] { AsyncPlayerPreLoginEvent.class, PlayerJoinEvent.class, PlayerKickEvent.class, PlayerLoginEvent.class, PlayerPreLoginEvent.class, PlayerQuitEvent.class });
	private boolean isExemptEnabled = true;
	private Map<String, Set<String>> pluginNameToWorlds = new HashMap<>();

	public void onLoad() {
		PerWorldPlugin.instance = this;
		Server.getPlugin().getLogger().info("Registering event interceptor...");
		PerWorldPluginLoader pwpLoader = new PerWorldPluginLoader(Bukkit.getServer());
		injectExistingPlugins(pwpLoader);
		cleanJavaPluginLoaders(pwpLoader);
	}

	private void injectExistingPlugins(PerWorldPluginLoader pwpLoader) {
		for (org.bukkit.plugin.Plugin p : Bukkit.getPluginManager().getPlugins()) {
			if (p instanceof JavaPlugin) {
				JavaPlugin jp = (JavaPlugin) p;
				try {
					Field f = JavaPlugin.class.getDeclaredField("loader");
					f.setAccessible(true);
					f.set(jp, pwpLoader);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void cleanJavaPluginLoaders(PerWorldPluginLoader pwpLoader) {
		PluginManager spm = Bukkit.getPluginManager();
		try {
			Field field = spm.getClass().getDeclaredField("fileAssociations");
			field.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			@SuppressWarnings("unchecked")
			Map<Pattern, PluginLoader> map = (Map<Pattern, PluginLoader>) field.get(spm);
			Iterator<Map.Entry<Pattern, PluginLoader>> iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<Pattern, PluginLoader> entry = iter.next();
				if (entry.getValue() instanceof JavaPluginLoader) {
					entry.setValue(pwpLoader);
				}
			}
			field.set(spm, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onEnable() {
		this.reload();
		Server.getPlugin().getLogger().info("Enabled, attempting to inject CommandHandler...");
		try {
			Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			f.setAccessible(true);
			SimpleCommandMap oldCommandMap = (SimpleCommandMap) f.get(Bukkit.getServer());
			if (oldCommandMap.getClass().getPackage().getName().contains(Server.getPlugin().getDescription().getName().toLowerCase())) {
				return;
			}
			f.set(Bukkit.getServer(), new FakeCommandMap(oldCommandMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Set<String> fillMinigameToWorlds(ServerMinigame minigame) {
		Set<String> list = new HashSet<String>();
		String minigameStr = minigame.toString().toLowerCase();
		if (Server.getWarpConfig().getData().contains(minigameStr)) {
			String worldName = $.getZoneLocation(minigameStr).getWorld().getName();
			list.add(worldName);
			list.add(worldName + "_nether");
			list.add(worldName + "_the_end");
			return list;
		}
		return null;
	}

	public void fillMinigameToWorlds(Set<String> list, ServerMinigame minigame) {
		String minigameStr = minigame.toString().toLowerCase();
		if (Server.getWarpConfig().getData().contains(minigameStr)) {
			String worldName = $.getZoneLocation(minigameStr).getWorld().getName();
			list.add(worldName);
			list.add(worldName + "_nether");
			list.add(worldName + "_the_end");
		}
	}

	public void reload() {
		pluginNameToWorlds.clear();
		if (Link$.isPluginEnabled("GriefPrevention")) {
			Set<String> griefPrevention = fillMinigameToWorlds(ServerMinigame.SURVIVAL);
			Server.getPlugin().getLogger().info("GriefPrevention " + "\u00BB" + " " + Arrays.toString(griefPrevention.toArray(new String[0])));
			pluginNameToWorlds.put("GriefPrevention", griefPrevention);
		}
	}

	public boolean checkWorld(org.bukkit.plugin.Plugin plugin, World w) {
		if (plugin == null)
			return true;
		if (w == null)
			return true;
		String pluginName = plugin.getDescription().getName();
		Set<String> restrictedWorlds = pluginNameToWorlds.get(pluginName);
		if (restrictedWorlds == null)
			return true;
		return restrictedWorlds.contains(w.getName().toLowerCase());
	}

	public boolean checkWorld(org.bukkit.plugin.Plugin plugin, Event e) {
		if ((e instanceof PlayerEvent)) {
			PlayerEvent e1 = (PlayerEvent) e;
			if ((exemptEvents.contains(e.getClass())) && (instance.isExemptEnabled())) {
				return true;
			}
			return checkWorld(plugin, e1.getPlayer().getWorld());
		}
		if ((e instanceof BlockEvent)) {
			BlockEvent e1 = (BlockEvent) e;
			if (e1.getBlock() == null || e1.getBlock().getWorld() == null)
				return true;
			return checkWorld(plugin, e1.getBlock().getWorld());
		}
		if ((e instanceof InventoryEvent)) {
			InventoryEvent e1 = (InventoryEvent) e;
			if (e1.getView().getPlayer() == null || e1.getView().getPlayer().getWorld() == null)
				return true;
			return checkWorld(plugin, e1.getView().getPlayer().getWorld());
		}
		if ((e instanceof EntityEvent)) {
			EntityEvent e1 = (EntityEvent) e;
			if (e1.getEntity() == null || e1.getEntity().getWorld() == null)
				return true;
			return checkWorld(plugin, e1.getEntity().getWorld());
		}
		if ((e instanceof HangingEvent)) {
			HangingEvent e1 = (HangingEvent) e;
			if (e1.getEntity() == null || e1.getEntity().getWorld() == null)
				return true;
			return checkWorld(plugin, e1.getEntity().getWorld());
		}
		if ((e instanceof VehicleEvent)) {
			VehicleEvent e1 = (VehicleEvent) e;
			if (e1.getVehicle() == null || e1.getVehicle().getWorld() == null)
				return true;
			return checkWorld(plugin, e1.getVehicle().getWorld());
		}
		if ((e instanceof WeatherEvent)) {
			WeatherEvent e1 = (WeatherEvent) e;
			if (e1.getWorld() == null)
				return true;
			return checkWorld(plugin, e1.getWorld());
		}
		if ((e instanceof WorldEvent)) {
			WorldEvent e1 = (WorldEvent) e;
			if (e1.getWorld() == null)
				return true;
			return checkWorld(plugin, e1.getWorld());
		}
		if ((e instanceof ServerEvent)) {
			return true;
		}
		return true;
	}

	public boolean isExemptEnabled() {
		return this.isExemptEnabled;
	}

	public static String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public static class PerWorldListener extends RegisteredListener {
		public PerWorldListener(Listener listener, EventExecutor executor, EventPriority priority, Plugin plugin, boolean ignoreCancelled) {
			super(listener, executor, priority, plugin, ignoreCancelled);
		}

		public void callEvent(Event event) throws EventException {
			if (event instanceof PlayerJoinEvent && getPlugin().getName().equals("GriefPrevention"))
				return;
			if (!me.skorrloregaming.PerWorldPlugin.instance.checkWorld(super.getPlugin(), event))
				return;
			try {
				super.callEvent(event);
			} catch (Throwable ex) {
				Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not pass event " + event.getEventName() + " to " + getPlugin().getDescription().getFullName(), ex);
			}
		}
	}

	public static class PerWorldTimedListener extends TimedRegisteredListener {
		public PerWorldTimedListener(Listener pluginListener, EventExecutor eventExecutor, EventPriority eventPriority, Plugin registeredPlugin, boolean listenCancelled) {
			super(pluginListener, eventExecutor, eventPriority, registeredPlugin, listenCancelled);
		}

		public void callEvent(Event event) throws EventException {
			if (event instanceof PlayerJoinEvent && getPlugin().getName().equals("GriefPrevention"))
				return;
			if (!me.skorrloregaming.PerWorldPlugin.instance.checkWorld(getPlugin(), event))
				return;
			try {
				super.callEvent(event);
			} catch (Throwable ex) {
				Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not pass event " + event.getEventName() + " to " + getPlugin().getDescription().getFullName(), ex);
			}
		}
	}

	public static class FakeCommandMap extends SimpleCommandMap {
		public SimpleCommandMap oldMap;
		private final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", 16);

		public FakeCommandMap(SimpleCommandMap oldCommandMap) {
			super(Bukkit.getServer());
			oldMap = oldCommandMap;
			Class<?> c = oldMap.getClass();
			if (oldMap instanceof FakeCommandMap)
				c = c.getSuperclass();
			for (Field f : c.getDeclaredFields()) {
				try {
					if (this.getClass().getSuperclass().getDeclaredField(f.getName()) != null) {
						transferValue(f.getName(), oldMap, c);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private void transferValue(String field, SimpleCommandMap oldMap, Class<?> c) {
			try {
				Field modifiers = Field.class.getDeclaredField("modifiers");
				modifiers.setAccessible(true);
				Field oldField = c.getDeclaredField(field);
				oldField.setAccessible(true);
				modifiers.setInt(oldField, oldField.getModifiers() & ~Modifier.FINAL);
				Object oldObject = oldField.get(oldMap);
				Field newField = this.getClass().getSuperclass().getDeclaredField(field);
				newField.setAccessible(true);
				modifiers.setInt(newField, newField.getModifiers() & ~Modifier.FINAL);
				newField.set(this, oldObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public boolean dispatch(CommandSender sender, String commandLine) throws CommandException {
			String[] args = PATTERN_ON_SPACE.split(commandLine);
			if (args.length == 0) {
				return false;
			}
			String sentCommandLabel = args[0].toLowerCase();
			Command target = getCommand(sentCommandLabel);
			if (target == null) {
				return false;
			}
			try {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					org.bukkit.plugin.Plugin plugin = null;
					if (target instanceof PluginIdentifiableCommand) {
						PluginIdentifiableCommand t = (PluginIdentifiableCommand) target;
						if (!PerWorldPlugin.instance.checkWorld(t.getPlugin(), p.getWorld()))
							plugin = t.getPlugin();
					}
					if (target.getClass().getSimpleName().equals("MCoreBukkitCommand")) {
						if (Link$.isPluginEnabled("MassiveCore"))
							plugin = Bukkit.getPluginManager().getPlugin("MassiveCore");
					}
					if (!PerWorldPlugin.instance.checkWorld(plugin, p.getWorld())) {
						p.sendMessage($.getMinigameTag(p) + ChatColor.RED + "This minigame prevents use of this command.");
						return true;
					}
				}
				target.execute(sender, sentCommandLabel, (String[]) Arrays.copyOfRange(args, 1, args.length));
			} catch (CommandException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new CommandException("Unhandled exception executing '" + commandLine + "' in " + target, ex);
			}
			return true;
		}
	}

	public class PerWorldPluginLoader implements PluginLoader {
		private JavaPluginLoader internal_loader;
		private org.bukkit.Server server;

		public PerWorldPluginLoader(org.bukkit.Server instance) {
			this.server = instance;
			internal_loader = new JavaPluginLoader(instance);
		}

		@Override
		public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, Plugin plugin) {
			Validate.notNull(plugin, "Plugin can not be null");
			Validate.notNull(listener, "Listener can not be null");
			boolean useTimings = server.getPluginManager().useTimings();
			Map<Class<? extends Event>, Set<RegisteredListener>> ret = new HashMap<Class<? extends Event>, Set<RegisteredListener>>();
			Set<Method> methods;
			try {
				Method[] publicMethods = listener.getClass().getMethods();
				methods = new HashSet<Method>(publicMethods.length, Float.MAX_VALUE);
				for (Method method : publicMethods) {
					methods.add(method);
				}
				for (Method method : listener.getClass().getDeclaredMethods()) {
					methods.add(method);
				}
			} catch (NoClassDefFoundError e) {
				plugin.getLogger().severe("Plugin " + plugin.getDescription().getFullName() + " has failed to register events for " + listener.getClass() + " because " + e.getMessage() + " does not exist.");
				return ret;
			}
			for (final Method method : methods) {
				final EventHandler eh = method.getAnnotation(EventHandler.class);
				if (eh == null)
					continue;
				final Class<?> checkClass;
				if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
					plugin.getLogger().severe(plugin.getDescription().getFullName() + " attempted to register an invalid EventHandler method signature \"" + method.toGenericString() + "\" in " + listener.getClass());
					continue;
				}
				final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
				method.setAccessible(true);
				Set<RegisteredListener> eventSet = ret.get(eventClass);
				if (eventSet == null) {
					eventSet = new HashSet<RegisteredListener>();
					ret.put(eventClass, eventSet);
				}
				for (Class<?> clazz = eventClass; Event.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()) {
					if (clazz.getAnnotation(Deprecated.class) != null) {
						Warning warning = clazz.getAnnotation(Warning.class);
						WarningState warningState = server.getWarningState();
						if (!warningState.printFor(warning)) {
							break;
						}
						plugin.getLogger().log(Level.WARNING, String.format("\"%s\" has registered a listener for %s on method \"%s\", but the event is Deprecated." + " \"%s\"; please notify the authors %s.", plugin.getDescription().getFullName(), clazz.getName(), method.toGenericString(), (warning != null && warning.reason().length() != 0) ? warning.reason() : "Server performance will be affected", Arrays.toString(plugin.getDescription().getAuthors().toArray())), warningState == WarningState.ON ? new AuthorNagException(null) : null);
						break;
					}
				}
				EventExecutor executor = new EventExecutor() {
					public void execute(Listener listener, Event event) throws EventException {
						try {
							if (!eventClass.isAssignableFrom(event.getClass())) {
								return;
							}
							method.invoke(listener, new Object[] { event });
						} catch (InvocationTargetException ex) {
							throw new EventException(ex.getCause());
						} catch (Throwable t) {
							throw new EventException(t);
						}
					}
				};
				if (useTimings) {
					eventSet.add(new PerWorldPlugin.PerWorldTimedListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled()));
				} else {
					eventSet.add(new PerWorldPlugin.PerWorldListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled()));
				}
			}
			return ret;
		}

		@Override
		public void disablePlugin(Plugin arg0) {
			internal_loader.disablePlugin(arg0);
		}

		@Override
		public void enablePlugin(Plugin arg0) {
			internal_loader.enablePlugin(arg0);
		}

		@Override
		public PluginDescriptionFile getPluginDescription(File arg0) throws InvalidDescriptionException {
			return internal_loader.getPluginDescription(arg0);
		}

		@Override
		public Pattern[] getPluginFileFilters() {
			return internal_loader.getPluginFileFilters();
		}

		@Override
		public Plugin loadPlugin(File arg0) throws InvalidPluginException, UnknownDependencyException {
			return internal_loader.loadPlugin(arg0);
		}
	}
}
