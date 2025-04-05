# MGSA Code
This Github repo contains the code for the MGSA office draw and MGSA elections.
## How do I office draw?
Congratulations budding office czar! Right now, running the office draw requires cloning this repo and modifying hardcoded directory paths in the code, but we're hoping to change that soon :)
### Help! The department redesignated which offices are graduate student offices!
No worries. Simply point the "activeoffices" entry in [data.json](data/data.json) to a modified list of active offices. If any of these offices are not yet present in [evans.json](data/evans.json), then you will need to add new entries. But please do not delete old entries as that will break previous years.
### Help! The we're moving out of Evans!
My condolences. You'll need to point the "floorplan" entry in [data.json](data/data.json) to a remake of [evans.json](data/evans.json). You will also need to make new floor plan images. Please do this carefully with attention to detail since these floor plan images will be used long after you're gone. Our current floor plan images are courtesy of Larsen Linov and are perhaps the most accurate maps of Evans Hall that you can find.
## How do I election?
Congratulations budding election czar! Right now, running the election requires cloning this repo and modifying hardcoded directory paths in the code, but we're hoping to change that soon :)
