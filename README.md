# SmugAware_v2_android
Updated code for Android.

It is strongly suggested that you 'refactor' your project as 'AndroidX' to alleviate any possible 'Manifest Merger' errors.

This was written with a Minimum SDK of 21, and a target of 28.

use it however you would launch a new Fragment.  In this particular instance, I used this:

Fragment fragment;

myURL = "https://www.yourserver.com/path/to/flightaware/data.json";

fragment = swSmugAware.newInstance(myURL);


This whole thing is to display (on a mobile phone screen) a map with Airplane activity in your area. It is driven by json data created by a "Pi Aware" Raspberry Pi computer configured as an ADS-B receiver running 'Dump 1090'.  There is iOS code around here somewhere too.

I build the receiever for fun, and used it as the basis for my 'Guam Airport Guide' that gives details about our local airport; flight schedules, etc... and it also has this neat 'Live' display of airplane activity (provided they have an ADS-B Transponder... mostly this is only the big planes)

You can find more information on that at https://flightaware.com/adsb/piaware/

