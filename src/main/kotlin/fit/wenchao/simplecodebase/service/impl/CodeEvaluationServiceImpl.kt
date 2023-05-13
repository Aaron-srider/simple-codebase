package fit.wenchao.simplecodebase.service.impl

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.Bind
import com.github.dockerjava.api.model.Volume
import com.github.dockerjava.core.DockerClientBuilder
import com.github.dockerjava.core.command.ExecStartResultCallback
import fit.wenchao.simplecodebase.rest.CodeEvaluationRequest
import fit.wenchao.simplecodebase.service.CodeEvaluationService
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.annotation.PostConstruct
import kotlin.io.path.pathString


@Service
class CodeEvaluationServiceImpl : CodeEvaluationService {

    lateinit var dockerClient: DockerClient

    @PostConstruct
    fun init() {
        dockerClient = DockerClientBuilder.getInstance().build()
    }

    override fun evaluateCode(request: CodeEvaluationRequest): String {
        return when (request.language.lowercase(Locale.getDefault())) {
            "java" -> compileAndRunJava(request.code)
            "kotlin" -> compileAndRunKotlin(request.code)
            else -> throw IllegalArgumentException("Unsupported language")
        }
    }

    private fun extractClassName(code: String): String {
        val classNamePattern: Pattern = Pattern.compile("public\\s+class\\s+(\\w+)")
        val matcher: Matcher = classNamePattern.matcher(code)
        return if (matcher.find()) {
            matcher.group(1)
        } else {
            throw java.lang.IllegalArgumentException("Invalid Java code: could not find public class")
        }
    }

    private fun compileAndRunJava(code: String): String {

        try {

            // Extract the class name from the code
            val className: String = extractClassName(code)


            val property = System.getProperty("java.io.tmpdir")

            var folderUuid = property + "/" + UUID.randomUUID().toString()

            Files.createDirectories(Paths.get(folderUuid))

            var tempSrcFile = File("$folderUuid/$className.java")

            // Write code to a temporary file

            val sourceFile = Files.createFile(tempSrcFile.toPath())
            Files.write(sourceFile, code.toByteArray())
            // Create a Docker container and mount the temporary file
            val volume = Volume("/usr/src/myapp")
            val container = dockerClient.createContainerCmd("java-eval")
                .withVolumes(volume)
                .withBinds(Bind(sourceFile.parent.toString(), volume))
                .exec()

            // Start the container
            dockerClient.startContainerCmd(container.id).exec()

            // Compile and run the Java code inside the container
            val compileCmd = arrayOf("javac", "/usr/src/myapp/${sourceFile.fileName}")
            val runCmd = arrayOf("java", "-cp", "/usr/src/myapp", sourceFile.fileName.toString().replace(".java", ""))
            val output = executeCommands(container.id, compileCmd, runCmd)

            // Cleanup
            dockerClient.removeContainerCmd(container.id).withForce(true).exec()
            recursiveDelete(sourceFile.toAbsolutePath().pathString)

            return output
        } catch (e: IOException) {
            throw RuntimeException("Error executing Java code", e)
        } catch (e: InterruptedException) {
            throw RuntimeException("Error executing Java code", e)
        }
    }
    fun recursiveDelete(directory: String) {
        val path = Paths.get(directory)
        Files.walk(path)
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach { it.delete() }
    }
    private fun executeCommands(containerId: String, vararg commands: Array<String>): String {
        val output = StringBuilder()
        for (command in commands) {
            val stdout = ByteArrayOutputStream()
            val stderr = ByteArrayOutputStream()
            dockerClient.execStartCmd(
                dockerClient.execCreateCmd(containerId)
                    .withAttachStdout(true)
                    .withAttachStderr(true)
                    .withCmd(*command)
                    .exec()
                    .id
            )
                .exec(ExecStartResultCallback(stdout, stderr))
                .awaitCompletion()
            output.append(stdout.toString())
            output.append(stderr.toString())
        }
        return output.toString()
    }

    private fun compileAndRunKotlin(code: String): String {
        // Compile Kotlin code and run using KotlinCompiler and ProcessBuilder
        return "Hello World!"
    }
}