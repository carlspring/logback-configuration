This module provides a simple wrapper over REST API for the `LoggingManagementService`.

You will need to sub-class the `AbstractLoggingManagementRestlet` and define a `@Path` on the class level
in order to specify the desired path to your restlet.

In your Spring context file, you may need to add something along the lines of:

    <context:property-placeholder ignore-resource-not-found="true" />

You can control the location of the logs dir via the `logging.dir` property. This defaults to `logs`.

You can control the location of the `logback.xml` file via the `logging.config.file` property. This defaults to `logback.xml`.
