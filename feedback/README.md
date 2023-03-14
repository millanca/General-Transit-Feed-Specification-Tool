# Instructor Feedback 
## Week 5 Lab
### Initial Impressions
* Project builds cleanly
  - Except for a commit that changed Stop.getStopid() to an int without compling the code.
    + Oops, Chris!
* Project runs, main window shows
* I would prefer to open all files at the same time, although it is nice that you tell me which 
file to select -- IT IS ESPECIALLY NICE THAT YOU TELL ME IN TWO PLACE IN CASE I MISS ONE.
* BAD SIGN: I cannot open La Crosse (GTFS_LAX):
        Caused by: java.lang.NumberFormatException: For input string: "5:"
            at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
            at java.lang.Integer.parseInt(Integer.java:580)
            at java.lang.Integer.parseInt(Integer.java:615)
            at gtfs.FileIO.importAll(FileIO.java:75)
            at gtfs.GTFSController.onImportAll(GTFSController.java:67)
            ... 58 more
  - This line seems to expect the hour to always be two digits, when it isn't.
* THIS IS GOOD: THE MILWAUKEE FILES APPEAR TO LOAD CORRECTLY.   I don't see any stop times in the
 printouts, however.
* I would prefer to not be warned that operations could "take a few minutes."  Perhaps there is a
 faster way to load the files? Maybe you want to look at the Big-O of your operations?
   - Loading MCTS does seem to take a really long time...
* I recommend avoiding classnames like "FileIO.java" What file(s) does this load? Perhaps this 
functionality should be pushed into Map, Stop, StopOnTrip, Route, etc.
* In FileIO.java, this if statement is true awfully often:
                  if(matchingStop.isEmpty()||matchingTrip.isEmpty()){
                      System.out.println("Skipped");
                  }

## Weeks 6 & 7 Labs
### First impressions   
* The master branch built and ran cleanly for me. Keep up the good work!
* The MCTS files seem to take a long time to load.
* The three members contributing this week contributed tests. The tests feel quite light -- 
clearly do NOT have full coverage.  But still an OK number of tests for each contributor.
* I do NOT see a README.md in the root of your directory as required in 
  https://faculty-web.msoe.edu/yoder/se2030/lab7res/SE2030Lab7.pdf
  I'm not sure which features your team intended to implement for Lab 7.
* I can only see Feature 3 implemented.
  Feature 3: It would be good to report units in the GUI. The times reported seem too high to me.
  Can you check your use of units in the calculations?
* It looks like you are doing input validation (Option 3).  Your code correctly detected duplicate 
IDs in a test. It also checks for invalid 
