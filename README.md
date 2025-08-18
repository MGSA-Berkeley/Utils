# MGSA Utilities
This Github repository contains the code for the MGSA office draw and MGSA elections.
## How do I run the office draw?
Congratulations budding office czar! Here is a step-by-step guide:
1. Clone this github repository to your computer with `git clone https://github.com/MGSA-Berkeley/Utils.git`.
2. For the Spring office draw, you will need to add a new entry to [data.json](officedraw/data/data.json). You can copy the most recent entry, but you should update `year`, `people`, and `start` (the unix time of the start of the office draw in milliseconds). The `people20xx.json` file will be created automatically by [MGSA.jar](MGSA.jar), so it's ok that it doesn't exist yet.
3. Before the office draw, run [MGSA.jar](MGSA.jar) and start filling in the first six columns with names of graduate students, which year they will be entering, their individual priority (base priority minus priority adjustement), their priority adjustment, their blocking group (either `Squat` or `Float` or `Block XYZ`), and their office if they are squatting. If this seems tedious, you can also directly copy and paste these six columns from a spreadsheet into the corresponding `20xx.officedraw` file.
4. The seventh column will alert you to any issues with the data. Clean-up the data until the only remaining warnings are `No history` (e.g., for transfer students) and `Skipped n years` (e.g., for students who were away for military service). But usually a warning like `No history` indicates that the student's name doesn't exactly match the name they gave in past years (e.g., due to nicknames or middle names) in which case you will need to manually resolve this discrepancy.
5. Press `CTRL-S` to save the current year's data. This will create or overwrite the corresponding `people20xx.json` file. If you changed any past years, you will need to save those changes as well.
6. Run `git add .` (to add the newly generated `people20xx.json` file), `git commit -am "commit message"` (to commit your changes locally), `git push` (to push your changes to this repository).
7. Log in to `mgsa@math.berkeley.edu` and upload the contents of [data](officedraw/data) to `public_html/officedraw/data` (or just upload the current `people20xx.json` file if that's all that was changed).
8. Check that the new data is present on the [officedraw website](https://math.berkeley.edu/~mgsa/officedraw/main.html).
9. During the office draw, run [MGSA.jar](MGSA.jar), click on the `Block` heading to sort by block, and start filling in the `Office` column.
10. Throughout the officedraw, run steps 5 through 8 to keep the website up-to-date.
### Help! The department redesignated which offices are graduate student offices!
No worries. Simply point the `activeoffices` entry in [data.json](officedraw/data/data.json) to a modified list of active offices. If any of these offices are not yet present in [evans.json](officedraw/data/evans.json), then you will need to add new entries. But please do not delete old entries as that will break previous years.
### Help! The we're moving out of Evans!
My condolences. You'll need to point the `floorplan` entry in [data.json](officedraw/data/data.json) to a remake of [evans.json](officedraw/data/evans.json). You will also need to make new floor plan images. Please do this carefully with attention to detail since these floor plan images will be used long after you're gone. Our current floor plan images are courtesy of Larsen Linov and are perhaps the most accurate maps of Evans Hall that you can find publically.
## How do I run the election?
Congratulations budding election czar! Right now, running the election requires cloning this repo and modifying hardcoded directory paths in the code, but we're hoping to change that soon :)
