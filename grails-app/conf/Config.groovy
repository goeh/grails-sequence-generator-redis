// configuration for plugin testing - will not be included in the plugin zip

log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error 'org.codehaus.groovy.grails.web.servlet',  //  controllers
            'org.codehaus.groovy.grails.web.pages', //  GSP
            'org.codehaus.groovy.grails.web.sitemesh', //  layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping', // URL mapping
            'org.codehaus.groovy.grails.commons', // core / classloading
            'org.codehaus.groovy.grails.plugins', // plugins
            'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
            'org.springframework',
            'org.hibernate',
            'net.sf.ehcache.hibernate'

    warn 'org.mortbay.log'

    debug 'grails.app.services.grails.plugins.sequence'
    debug 'org.apache.http.wire'
}

sequence.flushInterval = 5
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"

grails {
    redis {
        poolConfig {
            maxActive = 100
            maxIdle = 10
            minIdle = 1
            maxWait = 10000
            numTestsPerEvictionRun = 3
            testOnBorrow = true
            testOnReturn = true
            testWhileIdle = true
            timeBetweenEvictionRunsMillis = 30000
        }
        // use a single redis server (use only if not using sentinel cluster)
        host = "localhost"
        port = 6379
        timeout = 5000 //default in milliseconds
        password = "password" //defaults to no password
    }

}