package com.zachary_moore.runner;

import com.zachary_moore.ktql.SchemaProcessor;
import com.zachary_moore.ktql.gen.Generator;
import com.zachary_moore.ktql.gen.KTQLMode;
import com.zachary_moore.ktql.spec.Schema;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Runner {
    public static void main(String[] args) throws Exception {
        String inputPath = args[0];
        String outputDir = args[1];
        String rawSchema = new String(Files.readAllBytes(Paths.get(inputPath)));
        TypeDefinitionRegistry registry = new SchemaParser().parse(rawSchema);
        Schema schema = new SchemaProcessor(registry).process();
        Generator generator = new Generator(schema);
        generator.generate(
                new File(outputDir),
                "com.zachary_moore",
                KTQLMode.INLINE_TRANSLATION
        );
    }
}
