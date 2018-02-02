# logback-configuration

[![Master Build Status](https://dev.carlspring.org/jenkins/buildStatus/icon?job=opensource/logback-configuration/master)](https://dev.carlspring.org/jenkins/blue/organizations/jenkins/opensource%2Flogback-configuration/activity?branch=master)
[![Known Vulnerabilities](https://snyk.io/test/github/carlspring/logback-configuration/badge.svg)](https://snyk.io/test/github/carlspring/logback-configuration/)

This is an API for configuring logback programatically.

Currently, the implementation contains:
* A service layer (using Spring) located in the logback-configuration-core module which provides methods to:
 * Add loggers based on a package, level and appenderName
 * Update an existing logger
 * Delete an existing logger
 * Resolve a log file
 * Resolve the Logback configuration file
 * Upload a Logback configuration file and reload it

* A simple REST implementation (using Jersey) located in the logback-configuration-rest module which provides a wrapper for the above-mentioned service.
