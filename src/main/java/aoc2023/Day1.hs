module Day1(main) where
import Lib
import Debug.Trace
import System.IO
import Text.Regex.Posix ( (=~) )
import Data.List(isPrefixOf)
import Data.Char(isDigit)

main = do
        handle <- Lib.getInput 1
        fullString <- hGetContents handle
        let lines = words fullString
            nums = map(filter isDigit) lines
            part1 = sum (map(\x -> read[head x, last x]) nums)
            nums2 = map insertNum lines
            part2 = sum(map(\x -> 10 * head x +last x :: Int) nums2)
        print part1
        print part2
        hClose handle



insertNum :: String -> [Int]
insertNum line
    | null line  = []
    | isPrefixOf "one"  line =  1:insertNum ( drop 2 line )
    | isPrefixOf "two"  line =  2:insertNum ( drop 2 line )
    | isPrefixOf "three" line =  3:insertNum( drop 4 line )
    | isPrefixOf "four" line =  4:insertNum ( drop 4 line )
    | isPrefixOf "five" line =  5:insertNum ( drop 3 line )
    | isPrefixOf "six"  line =  6:insertNum ( drop 3 line )
    | isPrefixOf "seven" line =  7:insertNum( drop 4 line )
    | isPrefixOf "eight" line =  8:insertNum( drop 4 line )
    | isPrefixOf "nine" line =  9:insertNum ( drop 3 line )
    | isDigit (head line) = read(take 1 line) : insertNum (tail line)
    | otherwise = insertNum(tail line)
