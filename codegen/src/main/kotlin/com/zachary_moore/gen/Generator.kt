package com.zachary_moore.gen

import com.zachary_moore.spec.Schema
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import java.io.File
import java.io.StringWriter
import java.util.*

class Generator(
    private val schema: Schema
) {

    fun generate(outputPath: File,
                 apolloPackagePrefix: String,
                 ktqlMode: KTQLMode) {
        val p = Properties()
        p.setProperty("file.resource.loader.path", "/Users/zsmoore/dev/KTQL/codegen/src/main/resources/")
        Velocity.init(p)
        val context = VelocityContext()
        context.put("schema", schema)
        context.put("apolloPrefix", apolloPackagePrefix)
        context.put("KTQLMODE", ktqlMode.toString())
        val template = Velocity.getTemplate("KTQL.vm")
        val writer = StringWriter()
        template.merge(context, writer)
        outputPath.parentFile.mkdirs()
        outputPath.createNewFile()
        outputPath.writeText(writer.toString())
    }
}