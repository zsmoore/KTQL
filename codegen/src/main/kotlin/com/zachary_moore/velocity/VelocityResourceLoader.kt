package com.zachary_moore.velocity

import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
import java.io.Reader

private const val VELOCITY_DIRECTORY = "velocity"
class VelocityResourceLoader : ClasspathResourceLoader() {
    override fun getResourceReader(name: String?, encoding: String?): Reader {
        return super.getResourceReader("$VELOCITY_DIRECTORY/$name", encoding)
    }
}