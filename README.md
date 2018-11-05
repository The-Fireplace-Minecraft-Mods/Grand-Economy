# Grand Economy - A server-side only economy mod

EnderPay adds economy to Forge servers.
By itself, it doesn't do much, but it does add a basic income system, as well as commands to interact with players' currency. This is intended for use by other mods.

By default the user gets 100 credits on first login, and 50 for each (real time) day, as long as they login at least once every 6 days.
Each day 1% of user balance is taken from him.
The amount of tax taken each day is configurable (as well as other values)

## Commands
/balance - Check account balance

/pay <player> <amount> - Pay another player money from your account
  
/wallet balance <player> - Check another player's account balance

/wallet give <player> - Add credits to player's account

/wallet take <player> - Take credits from player's account

/wallet set <player> - Set player's account balance

## Installation
1. Install Minecraft forge.
2. Put the mod's .jar file you downloaded into the mods directory.

## Permissions:
This mod is available under GPL2.
You are allowed to include it into mod packs of any kind, without asking for permission.
