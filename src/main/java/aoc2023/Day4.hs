module Day4(main) where
import Lib
import System.IO
import Debug.Trace
import Data.Bits
import Text.Regex.Posix((=~))
import Data.Set (Set, fromList, size, intersection)
import Data.List((!!))
import qualified Data.Set as Set

main = do
        handle <- getInput 4
        stuff <- hGetContents handle
        let lines' = lines stuff
            cards = map parse lines'
            matches = map (\(a,b) ->  size(intersection a b)) cards
            part1 = sum(map (\x ->  (1 `shiftL`x)  `div` 2 :: Int) matches)
            part2 = mapFold (map (\_ -> 1) matches) matches
        print part1
        print part2

parse :: String -> (Set Int, Set Int)
parse line = do
                let groups =  head (line =~ "Card +[0-9]+: (.+) \\| (.+)") :: [String]
                (fromList(map read (words (groups !! 1))), fromList(map read (words(groups !! 2))))

mapFold :: [Int] -> [Int] -> Int
mapFold _ [] = 0
mapFold counts matches = head counts + mapFold(map ( + (head counts)) (take (head matches) (tail counts)) ++ drop (head matches + 1) counts) (tail matches)
