# logback-configuration

[![Master Build Status][build-badge]][build-link]
[![Known Vulnerabilities][vulnerabilities-badge]][vulnerabilities-link]
[![Maven Central Release][release-core-badge]][release-core-link]
[![Maven Central Release][release-rest-badge]][release-rest-link]

[build-link]: https://jenkins.carlspring.org/blue/organizations/jenkins/opensource%2Flogback-configuration/activity?branch=master
[build-badge]: https://jenkins.carlspring.org/buildStatus/icon?job=opensource/logback-configuration/master
[vulnerabilities-link]: https://snyk.io/test/github/carlspring/logback-configuration/
[vulnerabilities-badge]: https://snyk.io/test/github/carlspring/logback-configuration/badge.svg
[release-core-link]: http://repo2.maven.org/maven2/org/carlspring/logging/logging-configuration-core/
[release-core-badge]: https://img.shields.io/maven-central/v/org.carlspring.logging/logback-configuration-core.svg?label=logback-configuration-core
[release-rest-link]: http://repo2.maven.org/maven2/org/carlspring/logging/logging-configuration-rest/
[release-rest-badge]: https://img.shields.io/maven-central/v/org.carlspring.logging/logback-configuration-rest.svg?label=logback-configuration-rest


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
