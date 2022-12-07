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

    class ParsingEngine() {

        val allDirectory = mutableSetOf<Directory>()
        var state = State.Initial

        val root = Directory("/")
        var activeDirectory = root

        fun process(line: String): Int {
            allDirectory.add(activeDirectory) //inefficient, we don't care about non-actives because size=0
            when (state) {
                State.Initial -> {
                    //no verification check
                    val (command, parameter) = parseCommand(line)
                    when (command) {
                        Command.ChangeDirectory -> {
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
                                        directory = Directory(parameter)
                                    }
                                    activeDirectory = directory
                                }
                            }
                        }

                        Command.List -> {
                            state = State.IsInListCommand
                        }
                    }

                }

                State.IsInListCommand -> {
                    if (line.startsWith("$")) {
                        state = State.Initial
                        return 0
                    } else {
                        val token = line.split(" ")
                        when (token[0]) {
                            "dir" -> { //assume there's only one dir per directory
                                val directory = Directory(token[1])
                                activeDirectory.add(directory)
                            }

                            else -> {
                                val file = File(token[1], token[0].toInt())
                                activeDirectory.add(file)
                            }
                        }
                    }
                }

            }

            return 1
        }


    }


    fun part1(input: List<String>): Int {

        val engine = ParsingEngine()

        var i = 0
        while (i < input.size) {
            val line = input[i]
            i += engine.process(line)
        }

        val overList = mutableListOf<Directory>()

        for (directory in engine.allDirectory) {
            if (directory.getSize() <= 100000) {
                overList.add(directory)
                System.err.println("N: ${directory.name} ${directory.getSize()}")
            }
        }

        return overList.sumOf { it.getSize() }
    }

    fun part2(input: List<String>): Int {

        val engine = ParsingEngine()

        var i = 0
        while (i < input.size) {
            val line = input[i]
            i += engine.process(line)
        }


        val totalCapacity = 70000000
        val unusedSpace = totalCapacity - engine.root.getSize()

        val filterList = mutableListOf<Directory>()
        var smallest: Directory = engine.root

        for (directory in engine.allDirectory) {
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
