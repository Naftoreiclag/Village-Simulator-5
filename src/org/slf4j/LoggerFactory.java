package org.slf4j;

// This class exists only to satisfy poly2tri
public class LoggerFactory
{
    public static Logger getLogger(Class _)
    {
        return new Logger();
    }
}
