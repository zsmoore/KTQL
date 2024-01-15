package com.zachary_moore.gen

import com.zachary_moore.spec.Schema
import com.zachary_moore.velocity.VelocityResourceLoader
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.apache.velocity.runtime.RuntimeConstants
import java.io.File
import java.io.StringWriter
import java.util.*

class Generator(
    private val schema: Schema
) {

    fun generate(outputDir: File,
                 apolloPackagePrefix: String,
                 ktqlMode: KTQLMode) {
        val p = Properties()
        p[RuntimeConstants.RESOURCE_LOADERS] = "file,class"
        p[RuntimeConstants.RESOURCE_LOADER + ".class." + RuntimeConstants.RESOURCE_LOADER_CLASS] =
            VelocityResourceLoader::class.java.name
        Velocity.init(p)
        outputDir.mkdirs()
        generateKTQL(outputDir.resolve("KTQL.kt"), apolloPackagePrefix, ktqlMode)
        if (ktqlMode == KTQLMode.INLINE_TRANSLATION || ktqlMode == KTQLMode.CLIENT_BASED) {
            generateKTQLEngine(outputDir.resolve("KTQLEngine.kt"), ktqlMode)
        }
    }

    private fun generateKTQL(ktqlOutputPath: File,
                             apolloPackagePrefix: String,
                             ktqlMode: KTQLMode) {
        val context = VelocityContext()
        context.put("schema", schema)
        context.put("apolloPrefix", apolloPackagePrefix)
        context.put("KTQLMODE", ktqlMode.toString())
        val template = when(ktqlMode) {
            KTQLMode.INLINE_TRANSLATION -> Velocity.getTemplate("KTQL_INLINE.vm")
            KTQLMode.FILE_GENERATION -> Velocity.getTemplate("KTQL_FILE_GENERATION.vm")
            else -> throw IllegalStateException("Only inline translation supported")
        }
        val writer = StringWriter()
        template.merge(context, writer)
        ktqlOutputPath.createNewFile()
        ktqlOutputPath.writeText(writer.toString())
    }

    private fun generateKTQLEngine(ktqlEngineOutputPath: File,
                                   ktqlMode: KTQLMode) {
        val template = when(ktqlMode) {
            KTQLMode.INLINE_TRANSLATION -> Velocity.getTemplate("KTQL_INLINE_ENGINE.vm")
            else -> throw IllegalStateException("Only inline translation supported")
        }
        val writer = StringWriter()
        template.merge(VelocityContext(), writer)
        ktqlEngineOutputPath.createNewFile()
        ktqlEngineOutputPath.writeText(writer.toString())
    }
}