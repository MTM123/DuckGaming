# SkorrloreGaming
The best minecraft server management solution available for general consumption.
Have you ever wanted a plugin that could do the work of hundreds efficiently with low memory usage? Well then this plugin will satisfy your needs. With over 90 commands bundled with the plugin to make managing the server very easy, you can't go wrong.
You can easily contribute to the project if you so desire by forking the project, making your changes, and submitting a pull request.

# Features
* Over 90 commands for use in-game to help staff members efficiently manage the server from chat.
* Non-volatile player data for every storage-enhanced minigame.
* Commands and events are processed only if the current world allows it.
* Custom anti-cheat detects hackers more reliably then the competition.
* Custom votifier receives votes in record time and fairly calculates the rewards.
* Skins are automatically retrieved from mojang and forced onto players as they join.
* Everything fetched from the mojang api and the ip-api are cached to disk.
* Custom session manager allows players to invalidate sessions for security.
* Custom sign shop works as a replacement to official shop plugins.
* Http server hosted locally to show the top 5 voters for the current month.
* Custom economy allows players to have a bank account for every economy-enabled minigame.
* Custom vault economy handler makes it so other plugins can interface with our custom economy.
* Custom playtime manager makes it possible to see the playtime of a player in a calendar format.
* Custom npc manager lets staff create npc's at their current location that execute a specified task.
* Custom bot and proxy protection stops dangerous players from joining the server on the spot.
* Several custom mods designed by the community for the community makes playing on the server more fun.
* Custom notification worker lets staff communicate with other people on the server from their desktop.
* Custom scoreboard manager solves the dreaded problem of flickering whenever the scoreboard updates.
* Custom ping injector lets staff change everything that is displayed about the server on the server list. 
* Custom reflection class with a great deal of abstraction makes it easy for developers to mess with nms.
* Custom explosion makes it possible to use explosions such as creeper eggs to easily blow up an ally's base.

# Configuration
```Yaml
settings:
    enable:
        factions: true
        survival: true
        kitpvp: true
        skyfight: true
        creative: true
        skyblock: false
        prison: false
        welcomeMessage: true
        notificationWorker: false
        pingInjector: true
        preloadChunks: false
        authme:
            dailyAuth: true
            autoLoginCmd: true
        protocolsupport:
            versions:
                PC:
                    1m13: true
                    1m12: true
                    1m11: true
                    1m10: true
                    1m9: true
                    1m8: true
                    1m7: true
                    1m6: false
                    1m5: false
                    1m4: false
                PE: true
    ranking:
        enable: true
        prefix: true
    customJoinMessage: true
    customQuitMessage: true
    topVotersHttpServerPort: 2096
```

# Compiling
This project uses Maven 3 to manage dependencies, packaging, and shading of necessary classes.

The typical command used to build this project is: `mvn clean package install`

# Copyright
This project is licensed under the terms of [GNU Affero General Public License v3](https://github.com/java-mandelbrot/mandelbrot/blob/master/LICENSE)