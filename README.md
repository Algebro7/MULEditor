# MULEditor
A work-in-progress tool for comparing Battletech 'Mech BVs between various sources 

## Getting Started
MULEditor loads both sswlib and megamek as dependencies for loading 'Mech BVs and thus requires
them to be built and published to a local maven repository before MULEditor can be built.

### Preparing sswlib
Download the SSW source code and publish it to your local maven repo:

```
git clone https://github.com/Solaris-Skunk-Werks/SolarisSkunkWerks
cd SolarisSkunkWerks && git checkout develop
./gradlew :sswlib:publish
```

Also download the SSW Master 'Mech files:

```
git clone https://github.com/Solaris-Skunk-Werks/SSW-Master
```

### Preparing Megamek
Download the MegaMek source code and publish to your local maven repo:

```
git clone https://github.com/MegaMek/megamek
cd megamek && git checkout v0.47.14
./gradlew publishToMavenLocal
```

Note that if you want to compare BVs with megamek, you'll need to run megamek once and pass the
`units.cache` file to MULEditor. This file can be found in `data/mechfiles` in the megamek install
directory.

### Building MULEditor
Download the MULEditor source code and build it:

```
git clone https://github.com/Algebro7/MULEditor
cd MULEditor && ./gradlew installDist
```

Extract the distribution and run the program:

```
cd build/distributions && unzip MULEditor-1.0.zip
cd MULEDitor-1.0/bin
./MULEditor compare -h
```

## Usage
Currently, MULEditor does not actually do any editing, though that feature may be implemented in the future. MULEditor
is designed to use subcommands, each with their own optional arguments to customize the behavior. The only subcommand
implemented at this time is `compare`, which must be supplied as the first argument:

```
./MULEditor compare -h
```

`compare` takes SSW 'Mech files from a local SSW-Master folder and compares the BVs with the MUL, which it can retrieve
from the MUL website at runtime or from a MUL cache file you've generated in previous runs. You can optionally add a 
Megamek cachefile to include Megamek BVs when discrepancies are found between SSW and the MUL. Output is saved in CSV
format in a file called `mismatched-bvs.csv` by default.

Note that currently, `--mul` is mandatory, although the option has been added to allow comparisons without the MUL once
that functionality is implemented.

To update the local MUL cache and compare BVs between the MUL, SSW, and Megamek:
```
./MULEditor compare -m ~/megamek/megamek/data/mechfiles/units.cache -s ~/SSW-Master --mul
```

To compare SSW and the MUL use a local MUL cache, saving the server admins some bandwidth:
```
./MULEditor compare -i mul.txt -s ~/SSW-Master --mul
```

General usage/help output:
```
Usage: MULEditor compare [--mul] [-b=<bvOutfile>] [-i=<infile>]
                         [-m=<mmCacheFile>] [-o=<outfile>] [-s=<sswMasterPath>]
  -b, --bv-file=<bvOutfile> Path to save mismatched BVs (default:
                              mismatched-bvs.csv)
  -i, --mul-in=<infile>     Path to saved MUL unit file
  -m, --megamek-cache-file=<mmCacheFile>
                            Path to megamek units.cache file
      --mul                 Compare with MUL (default: false)
  -o, --mul-out=<outfile>   Filename to write MUL entries (default: mul.txt)
  -s, --ssw-path=<sswMasterPath>
                            Path to SSW-Master folder
```