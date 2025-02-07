package me.skorrloregaming.hooks;

import fr.xephi.authme.events.FailedLoginEvent;
import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import protocolsupportpocketstuff.api.modals.ComplexForm;
import protocolsupportpocketstuff.api.modals.ModalCallback;
import protocolsupportpocketstuff.api.modals.elements.ElementResponse;
import protocolsupportpocketstuff.api.modals.elements.complex.ModalInput;
import protocolsupportpocketstuff.api.modals.elements.complex.ModalLabel;
import protocolsupportpocketstuff.api.util.PocketPlayer;

public class ProtocolSupportPocketStuff_Listener implements Listener {
    private ComplexForm loginModal;
    private ComplexForm faultyLoginModal;
    private ComplexForm closeLoginModal;
    private ComplexForm registerModal;
    private ComplexForm faultyRegisterModal;
    private ComplexForm closeRegisterModal;
    private ComplexForm notMatchRegisterModal;
    private ModalCallback loginCallback;
    private ModalCallback registerCallback;

    public void register() {
        Server.getInstance().getPlugin().getServer().getPluginManager().registerEvents(this, Server.getInstance().getPlugin());
    }

    public void bakeModals() {
        loginModal = new ComplexForm("Login").addElement(new ModalLabel("You must login to play on this server.")).addElement(new ModalInput(" ").setPlaceholderText("Password"));
        faultyLoginModal = loginModal.clone();
        faultyLoginModal.getElements().get(0).setText("Wrong password, please try again.");
        closeLoginModal = loginModal.clone();
        closeLoginModal.getElements().get(0).setText("You must login to play on this server.");
        registerModal = new ComplexForm("Register").addElement(new ModalLabel("You must register to play on here.")).addElement(new ModalInput(" ").setPlaceholderText("Password")).addElement(new ModalInput(" ").setPlaceholderText("Confirm Password"));
        faultyRegisterModal = registerModal.clone();
        faultyRegisterModal.getElements().get(0).setText("Your password isn't long enough, try again.");
        closeRegisterModal = registerModal.clone();
        closeRegisterModal.getElements().get(0).setText("You must register to play on here.");
        notMatchRegisterModal = registerModal.clone();
        notMatchRegisterModal.getElements().get(0).setText("Your passwords do not match, try again.");
    }

    public fr.xephi.authme.api.v3.AuthMeApi getAuthMeApi() {
        return ((fr.xephi.authme.api.v3.AuthMeApi) $.getAuthenticationSuite());
    }

    public void bakeCallbacks() {
        loginCallback = response -> {
            if (!response.isCancelled()) {
                ElementResponse password = response.asComplexFormResponse().getResponse(1);
                if (getAuthMeApi().checkPassword(response.getPlayer().getName(), password.getString().trim())) {
                    getAuthMeApi().forceLogin(response.getPlayer());
                    Bukkit.getScheduler().runTaskLater(Server.getInstance().getPlugin(), () -> {
                    }, 20L);
                } else {
                    PocketPlayer.sendModal(response.getPlayer(), faultyLoginModal, loginCallback);
                }
            } else {
                PocketPlayer.sendModal(response.getPlayer(), closeLoginModal, loginCallback);
            }
        };
        registerCallback = response -> {
            if (!response.isCancelled()) {
                ElementResponse password = response.asComplexFormResponse().getResponse(1);
                ElementResponse confirm = response.asComplexFormResponse().getResponse(2);
                if (password.getString().trim().equals(confirm.getString().trim())) {
                    if (password.getString().length() < 4 || password.getString().length() > 16) {
                        PocketPlayer.sendModal(response.getPlayer(), faultyRegisterModal, registerCallback);
                    } else {
                        getAuthMeApi().forceRegister(response.getPlayer(), password.getString().trim());
                        getAuthMeApi().forceLogin(response.getPlayer());
                        Bukkit.getScheduler().runTaskLater(Server.getInstance().getPlugin(), () -> Server.getInstance().fetchLobby(response.getPlayer()), 20L);
                    }
                } else {
                    PocketPlayer.sendModal(response.getPlayer(), notMatchRegisterModal, registerCallback);
                }
            } else {
                PocketPlayer.sendModal(response.getPlayer(), closeRegisterModal, registerCallback);
            }
        };
    }

    public void onLogin(PlayerJoinEvent event) {
        if (!PocketPlayer.isPocketPlayer(event.getPlayer())) {
            Server.getInstance().fetchLobby(event.getPlayer());
            return;
        }
        Server.getInstance().getBukkitTasks().add(Bukkit.getScheduler().runTaskLater(Server.getInstance().getPlugin(), () -> {
            if (event.getPlayer() != null && event.getPlayer().isOnline() && !getAuthMeApi().isAuthenticated(event.getPlayer())) {
                if (getAuthMeApi().isRegistered(event.getPlayer().getName())) {
                    PocketPlayer.sendModal(event.getPlayer(), loginModal, loginCallback);
                } else {
                    PocketPlayer.sendModal(event.getPlayer(), registerModal, registerCallback);
                }
            }
        }, 200L));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLoginFailure(FailedLoginEvent event) {
        if (PocketPlayer.isPocketPlayer(event.getPlayer())) {
            PocketPlayer.sendModal(event.getPlayer(), faultyLoginModal, loginCallback);
        }
    }
}
