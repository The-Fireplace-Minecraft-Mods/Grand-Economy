# Grand Economy API
## Adding Grand Economy to your dev environment
TODO

## Using the Entrypoint (for a Soft Dependency)
To use currency from Grand Economy and/or register your own currency with Grand Economy without creating a hard dependency on it, you'll want to use the entrypoint.

1. Create a class that implements `the_fireplace.grandeconomy.api.GrandEconomyEntrypoint`
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