# Grand Economy API
[![CurseForge](http://cf.way2muchnoise.eu/short_312141_downloads.svg)](https://minecraft.curseforge.com/projects/grand-economy)
## Adding Grand Economy to your dev environment
To use this with your mod, include the following in `build.gradle`:
```
dependencies {
  modImplementation "dev.the-fireplace:GrandEconomy:${project.grandeconomy_version}"
}
```
And in `gradle.properties`:
```
grandeconomy_version=<mod version>+<minecraft version>
```

## Using the Entrypoint (for a Soft Dependency)
To use currency from Grand Economy and/or register your own currency with Grand Economy without creating a hard dependency on it, you'll want to use the entrypoint.

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