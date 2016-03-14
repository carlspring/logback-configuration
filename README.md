This is an API for configuring logback programatically.

Currently, the implementation contains:
* A service layer (using Spring) located in the logback-configuration-core module which provides methods to:
** Add loggers based on a package, level and appenderName
** Update an existing logger
** Delete an existing logger
** Resolve a log file
** Resolve the Logback configuration file
** Upload a Logback configuration file and reload it
* A simple REST implementation (using Jersey) located in the logback-configuration-rest module which provies a wrapper for the above-mentioned service.
