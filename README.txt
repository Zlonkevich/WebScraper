Create console web scraper (http://en.wikipedia.org/wiki/Web_scraping) utility which:
-          accepts as command line parameters:
o   (must) web resources URL or path to plain text file containing a list of URLs
o   (must) data command(s)
o   (must) word (or list of words with “,” delimiter)
o   (nice to have) output verbosity flag,  if on then the output should contains information about time spend on data scraping and data processing (-v)
-          supports the following data processing commands:
o   (must) count number of provided word(s) occurrence on webpage(s). (-w)
o   (must) count number of characters of each web page (-c)
o   (nice to have) extract sentences’ which contain given words (-e)
 
Data processing results should be printed to output for each web resources separately and for all resources as total.
Command line parameters example for Java implementation:
java –jar scraper.jar http://www.cnn.com Greece, default –v –w –c –e
 
Tips:
- Do not use 3rd party libraries
- Task fulfillment preferable programming languages is Java.
