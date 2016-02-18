package org.carlspring.logging.services;

/**
 * @author mtodorov
 */
public interface LoggingManagementService
{

    // TODO:
    // TODO: Add methods with proper parameters here. These are just examples.
    // TODO:


   int getStatus();

   void addLogger(String loggerPackage, String level);
   void updateLogger(String loggerPackage, String level);
   void deleteLogger(String loggerPackage);
}
