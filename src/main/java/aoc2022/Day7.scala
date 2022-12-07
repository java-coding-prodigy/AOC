package aoc2022

import scala.collection.mutable

object Day7 extends Day(7) {

  def main(args: Array[String]): Unit = super.run()

  class File(val size: Int, val name: String)

  class Directory(val name: String, val data: List[Directory | File])

  def find(dirName: String, startIdx: Int = 0): (Directory, Int) = {
    val dirData = mutable.ListBuffer.empty[Directory | File]
    var idx = startIdx + 1
    while (idx < input.length) {
      val line = input(idx).split(' ')
      if (line.head.equals("$")) {
        if (line(1).equals("cd")) {
          if (line(2).equals("..")) {
            return (new Directory(dirName, dirData.toList), idx)
          }
          else {
            val (dir, endIdx) = find(line(2), idx)
            dirData.addOne(dir)
            idx = endIdx
          }
        }
      } else {
        if (!line.head.equals("dir")) {
          dirData.addOne(new File(line.head.toInt, line(1)))
        }
      }
      idx += 1
    }
    (new Directory(dirName, dirData.toList), idx)
  }

  val topDirectory: Directory = find("/")._1
  val directorySizes: mutable.Map[Directory, Int] = mutable.HashMap.empty

  def findSize(directory: Directory): Int = {
    if (!directorySizes.contains(directory)) {
      for (data <- directory.data) {
        directorySizes.updateWith(directory)((size: Option[Int]) => Option(size.getOrElse(0) +
          (data match {
            case file: File => file.size
            case childDirectory: Directory => findSize(childDirectory)
          })))
      }
    }
    directorySizes(directory)
  }

  findSize(topDirectory)
  val sizes: Iterable[Int] = directorySizes.values
  val sizeNeeded: Int = 30000000 - (70000000 - directorySizes(topDirectory))

  override def part1(): Any = sizes.filter(_ < 100000).sum

  override def part2(): Any = sizes.filter(_ >= sizeNeeded).min
}
