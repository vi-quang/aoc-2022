import java.lang.IllegalStateException

/**
 * Main -------------------------------------------------------------------
 */
private enum class State {
    Initial,
    IsInListCommand
}

private enum class Command {
    ChangeDirectory,
    List
}

fun main() {

    fun parseCommand(line: String): Pair<Command, String> {
        val token = line.split(" ")
        val command = token[1]

        if (command == "cd") {
            return Command.ChangeDirectory to token[2]
        }

        if (command == "ls") {
            return Command.List to ""
        }

        throw IllegalStateException()
    }

    data class File(val name: String, val size: Int)
    class Directory(val name: String) {

        var parentDirectory: Directory? = null

        private val fileList = mutableListOf<File>()
        private var fileSize = 0
        fun add(file: File) {
            fileList.add(file)
            fileSize += file.size
        }

        private val directoryList = mutableListOf<Directory>()
        fun add(directory: Directory) {
            directoryList.add(directory)
            directory.parentDirectory = this
        }

        fun getDirectory(name: String): Directory? {
            for (directory in directoryList) {
                if (directory.name == name) {
                    return directory
                }
            }

            return null
        }

        fun getSize(): Int {
            var size = fileSize
            for (directory in directoryList) {
                size += directory.getSize()
            }

            return size
        }
    }

    class CommandParser() {


        val allDirectory = mutableSetOf<Directory>()
        var state = State.Initial

        val root = Directory("/")

        var activeDirectory = root

        init {
            allDirectory.add(root)
        }

        fun process(line: String): Int {

            when (state) {
                State.Initial -> {
                    //no verification check
                    processCommand(line)
                }

                State.IsInListCommand -> {
                    if (line.startsWith("$")) {
                        state = State.Initial
                        return 0
                    }

                    processListCommand(line)
                }
            }

            return 1
        }


        private fun processCommand(line: String) {
            val (command, parameter) = parseCommand(line)
            when (command) {
                Command.ChangeDirectory -> {
                    processChangeDirectoryCommand(parameter)
                }

                Command.List -> {
                    state = State.IsInListCommand
                }
            }
        }

        private fun processChangeDirectoryCommand(parameter: String) {
            when (parameter) {
                "/" -> {
                    activeDirectory = root
                }

                ".." -> {
                    val parentDirectory = activeDirectory.parentDirectory
                    if (parentDirectory == null) {
                        activeDirectory = root
                    } else {
                        activeDirectory = parentDirectory
                    }
                }

                else -> {
                    var directory = activeDirectory.getDirectory(parameter)
                    if (directory == null) {
                        directory = createDirectory(activeDirectory, parameter)
                    }
                    activeDirectory = directory
                }
            }
        }

        private fun processListCommand(line: String) {
            val token = line.split(" ")
            when (token[0]) {
                "dir" -> { //assume there's only one dir per directory
                    createDirectory(activeDirectory, token[1])
                }

                else -> {
                    createFile(activeDirectory, token[1], token[0].toInt())
                }
            }
        }

        private fun createFile(parent: Directory, name : String, size: Int) {
            val file = File(name, size)
            parent.add(file)
        }

        private fun createDirectory(parent: Directory, name: String) : Directory {
            val directory = Directory(name)
            parent.add(directory)
            allDirectory.add(directory)
            return directory
        }
    }

    class ParserEngine {

        var commandParser = CommandParser()

        fun process(input: List<String>) {
            var i = 0
            while (i < input.size) {
                val line = input[i]
                i += commandParser.process(line)
            }
        }


    }

    fun part1(input: List<String>): Int {

        val engine = ParserEngine()
        engine.process(input)

        val overList = mutableListOf<Directory>()

        for (directory in engine.commandParser.allDirectory) {
            if (directory.getSize() <= 100000) {
                overList.add(directory)
                System.err.println("N: ${directory.name} ${directory.getSize()}")
            }
        }

        return overList.sumOf { it.getSize() }
    }

    fun part2(input: List<String>): Int {

        val engine = ParserEngine()
        engine.process(input)

        val totalCapacity = 70000000
        val unusedSpace = totalCapacity - engine.commandParser.root.getSize()

        val filterList = mutableListOf<Directory>()
        var smallest: Directory = engine.commandParser.root

        for (directory in engine.commandParser.allDirectory) {
            val difference = directory.getSize() + unusedSpace

            if (difference >= 30000000) {
                filterList.add(directory)
                if (directory.getSize() < smallest.getSize()) {
                    smallest = directory
                }
                System.err.println("N: ${directory.name} ${directory.getSize()}")
            }
        }


        return smallest.getSize()
    }


    val input = readInput(7)
    println(part1(input))
    println(part2(input))
}
