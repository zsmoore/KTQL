package com.zachary_moore.gen

import com.zachary_moore.spec.Schema
import org.apache.commons.lang.StringUtils
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import java.io.StringWriter
import java.util.*

class Generator(
    private val schema: Schema
) {

    fun generate() {
        val p = Properties()
        p.setProperty("file.resource.loader.path", "/Users/zsmoore/dev/KTQL/codegen/src/main/resources/")
        Velocity.init(p)
        val context = VelocityContext()
        context.put("schema", schema)
        context.put("StringUtils", StringUtils::class.java)
        val template = Velocity.getTemplate("KTQL.vm")
        val writer = StringWriter()
        template.merge(context, writer)
        print(writer.toString())
    }
}