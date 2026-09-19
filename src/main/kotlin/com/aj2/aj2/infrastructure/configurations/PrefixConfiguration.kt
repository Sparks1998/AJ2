package com.aj2.aj2.infrastructure.configurations

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class PrefixConfiguration : WebMvcConfigurer {
    override fun configurePathMatch(configurer: PathMatchConfigurer) {
        configurer.addPathPrefix(
            "/admin",
        ) { handler: Class<*>? ->
            handler!!.`package`.name.startsWith("com.aj2.aj2.modules.admin.api")
        }
        configurer.addPathPrefix(
            "/jobs",
        ) { handler: Class<*>? ->
            handler!!.`package`.name.startsWith("com.aj2.aj2.modules.job.api")
        }
    }
}
