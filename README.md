# telegram-wlan-control

## build and install

```
./gradlew installDist
sudo make -C build/install/telegram-wlan-control install
```

## configure and run

1. create a new bot to get a token
2. create a group and add admins (may turn WLAN on)
3. add the bot to the group
4. create the config file in `/usr/local/etc/telegram-wlan-control.conf`

```
token=<token from step 1>
chatId=<numeric chatId from step 2>
```

NOTE: in order to get the numeric chat id, start the bot with chatId=-1, then
      get the real id from the updateLog in `/var/db/telegram-wlan-control`

## control the wlan

In order to do something useful, create the script `/etc/wlan-control` accepting
a single arg of either `on` or `off`.
