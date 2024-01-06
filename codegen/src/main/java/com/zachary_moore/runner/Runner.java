package com.zachary_moore.runner;

import com.zachary_moore.SchemaProcessor;
import com.zachary_moore.gen.Generator;
import com.zachary_moore.spec.Schema;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Runner {
    public static void main(String[] args) throws Exception {
        String inputPath = args[0];
        String outputPath = args[1];
        String rawSchema = new String(Files.readAllBytes(Paths.get(inputPath)));
        TypeDefinitionRegistry registry = new SchemaParser().parse(rawSchema);
        Schema schema = new SchemaProcessor(registry).process();
        Generator generator = new Generator(schema);
        generator.generate(new File(outputPath), "com.zachary_moore");
    }
}