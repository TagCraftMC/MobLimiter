MobLimiter 2
============

## Download

Compile (a Maven project) or download from our [build service](http://build.core-network.us:8080/job/MobLimiter%202/).

## Configuration

``` yaml
Animals:
  GlobalChunkLimit: 30
  GlobalViewDistanceLimit: 150
  GroupPlural: Animals
  HORSE:
    NameSingular: Horse
    NamePlural: Horses
    ChunkLimit: 8
    ViewDistanceLimit: 150
  COW:
    NameSingular: Cow
    NamePlural: Cows
    ChunkLimit: 30
    ViewDistanceLimit: 150
  SHEEP:
    NameSingular: Sheep
    NamePlural: Sheep
    ChunkLimit: 30
    ViewDistanceLimit: 150
  CHICKEN:
    NameSingular: Chicken
    NamePlural: Chickens
    ChunkLimit: 30
    ViewDistanceLimit: 150
  PIG:
    NameSingular: Pig
    NamePlural: Pigs
    ChunkLimit: 30
    ViewDistanceLimit: 150
Villages:
  GroupPlural: Villagers and Golems
  GlobalChunkLimit: 20
  GlobalViewDistanceLimit: 150
  IRON_GOLEM:
    NameSingular: Iron Golem
    NamePlural: Iron Golems
    ChunkLimit: 4
    ViewDistanceLimit: 150
  VILLAGER:
    NameSingular: Villager
    NamePlural: Villagers
    ChunkLimit: 20
    ViewDistanceLimit: 150
ViewDistanceChunks: 6
Messages:
  NoPermission: '&cNo permission!'
  MobCountLine: '&6<MobName>: <ChunkCount>&7/<ChunkLimit> in chunk, &6<ViewDistanceCount>&7/<ViewDistanceLimit>
    in view distance.'
  TooManyMobs: '&cYou have too many mobs for the server to handle. Please, if you
    can, consider killing some or moving them further to keep the server healthy.
    Many thanks, we appreciate it.'
  BreedLimitOneMob: '&cYou cannot breed more than <MobTypeLimit> <MobNamePlural> in
    view distance.'
  BreedLimitAllMobs: '&cYou cannot breed more than <MobGroupLimit> <MobGroupNamePlural>
    in view distance.'
  NoHorseBreeding: '&cSorry, you are not allowed to breed horses. Find another one
    in the wild.'
NoHorseBreed: true
BreedingSpamDelaySeconds: 5
Grid: # contains settings for the grid
  None: # block id/data for 0% of limit
    Id: 20
    Data: 0
  Low: # block id/data for < 80% of limit
    Id: 35
    Data: 5
  Medium: # block id/data for > 80% of limit
    Id: 35
    Data: 4
  High: # block id/data for > 90% of limit
    Id: 35
    Data: 1
  Exceed: # block id/data for when the limit is exceeded
    Id: 35
    Data: 14
  Duration: 600 # the duration after which the grid will disappear automatically, in server ticks.
```

## Commands

There are 2 commands, which do the same thing but for different creature groups:

    /animals
    /villagers

The `/animals` command will count the animals group, that is cows, pigs, chicken, sheep and horses.

The `/villagers` command will count villagers and iron golems.

Both commands have the same arguments, the `/animals` command will be used as an example here,
but every argument will work the same way for the `villagers` command as well.

    /animals show

Will display a grid 10 blocks above the player, like depict in this image:

![Grid visualization of a count command](https://f.cloud.github.com/assets/616791/1810640/e3effa1a-6e2d-11e3-8563-1bc4077fd4d9.png)

The grid is aligned by chunk and there are 5 different layouts/colors that are drawn per chunk:

 * **Edges / transparent**: No creatures apply to counted group
 * **One bar in the middle / lime green**: Max creature count is below 80% of limit
 * **Two bars in the middle / yellow**: Max creature count is above 80% of limit
 * **Dotted border / orange**: Max creature count is above 90% of limit
 * **Solid border / red**: Max creature count exceeds limit

With default arguments, the grid will disappear after 30 seconds (this can be configured with the field `Grid.Duration`).

To make the grid stay, add the `keep` argument to the command, like so:

    /animals show keep

This will make the grid stay until you reload the chunks or type in this command:

    /animals hide

