# Grand Economy API
[![CurseForge](http://cf.way2muchnoise.eu/short_312141_downloads.svg)](https://minecraft.curseforge.com/projects/grand-economy)
[![Modrinth](https://img.shields.io/badge/dynamic/json?color=5da545&label=modrinth&prefix=downloads%20&query=downloads&url=https://api.modrinth.com/api/v1/mod/gno5mxtx&style=plastic&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxMSAxMSIgd2lkdGg9IjE0LjY2NyIgaGVpZ2h0PSIxNC42NjciICB4bWxuczp2PSJodHRwczovL3ZlY3RhLmlvL25hbm8iPjxkZWZzPjxjbGlwUGF0aCBpZD0iQSI+PHBhdGggZD0iTTAgMGgxMXYxMUgweiIvPjwvY2xpcFBhdGg+PC9kZWZzPjxnIGNsaXAtcGF0aD0idXJsKCNBKSI+PHBhdGggZD0iTTEuMzA5IDcuODU3YTQuNjQgNC42NCAwIDAgMS0uNDYxLTEuMDYzSDBDLjU5MSA5LjIwNiAyLjc5NiAxMSA1LjQyMiAxMWMxLjk4MSAwIDMuNzIyLTEuMDIgNC43MTEtMi41NTZoMGwtLjc1LS4zNDVjLS44NTQgMS4yNjEtMi4zMSAyLjA5Mi0zLjk2MSAyLjA5MmE0Ljc4IDQuNzggMCAwIDEtMy4wMDUtMS4wNTVsMS44MDktMS40NzQuOTg0Ljg0NyAxLjkwNS0xLjAwM0w4LjE3NCA1LjgybC0uMzg0LS43ODYtMS4xMTYuNjM1LS41MTYuNjk0LS42MjYuMjM2LS44NzMtLjM4N2gwbC0uMjEzLS45MS4zNTUtLjU2Ljc4Ny0uMzcuODQ1LS45NTktLjcwMi0uNTEtMS44NzQuNzEzLTEuMzYyIDEuNjUxLjY0NSAxLjA5OC0xLjgzMSAxLjQ5MnptOS42MTQtMS40NEE1LjQ0IDUuNDQgMCAwIDAgMTEgNS41QzExIDIuNDY0IDguNTAxIDAgNS40MjIgMCAyLjc5NiAwIC41OTEgMS43OTQgMCA0LjIwNmguODQ4QzEuNDE5IDIuMjQ1IDMuMjUyLjgwOSA1LjQyMi44MDljMi42MjYgMCA0Ljc1OCAyLjEwMiA0Ljc1OCA0LjY5MSAwIC4xOS0uMDEyLjM3Ni0uMDM0LjU2bC43NzcuMzU3aDB6IiBmaWxsLXJ1bGU9ImV2ZW5vZGQiIGZpbGw9IiM1ZGE0MjYiLz48L2c+PC9zdmc+)](https://modrinth.com/mod/grand-economy)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/dev.the-fireplace/GrandEconomy/badge.png)](https://maven-badges.herokuapp.com/maven-central/dev.the-fireplace/GrandEconomy)
## Adding Grand Economy to your dev environment
To use this with your mod, include the following in `build.gradle`:
```
repositories {
  maven {
	name 'Cloth Config'
	url 'https://maven.shedaniel.me/'
  }
  maven {
	name 'Mod Menu'
	url 'https://maven.terraformersmc.com/releases/'
  }
}
dependencies {
  modCompileOnly "dev.the-fireplace:Grand-Economy:${project.grandeconomy_version}:api"
  modRuntime "dev.the-fireplace:Grand-Economy:${project.grandeconomy_version}"
}
```
And in `gradle.properties`:
```
grandeconomy_version=<mod version>+<minecraft version>
```

## Using the Entrypoint
To use currency from Grand Economy and/or register your own currency, you'll want to use the entrypoint.

1. Create a class that implements `GrandEconomyEntrypoint`
2. Add whatever logic you want to in your `GrandEconomyEntrypoint`'s `init` function
3. Add your entrypoint class to the `grandeconomy` entrypoint in your `fabric.mod.json`

#### Example entrypoint
    "entrypoints": {
        "main": [
            ...
        ],
        "grandeconomy": [
            "your.package.YourImplementationClass"
        ]
    },