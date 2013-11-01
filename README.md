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
  MobCountLine: '&6<MobName>: <ChunkCount>&7/<ChunkLimit> in chunk,
 &6<ViewDistanceCount>&7/<ViewDistanceLimit>
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
```