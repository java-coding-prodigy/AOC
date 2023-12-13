module Day2 where
import Lib
import System.IO
import Data.Char(isLetter)
import Data.Map (Map)
import Data.Map((!), fromList, elems, insert)
import qualified Data.Map as Map



main = do
        handle <- getInput(2)
        stuff  <- hGetContents handle
        let lines' = lines stuff
            games = (map (\l -> parse(words l)) lines')
            part1 = sum (map fst (filter(\x -> all(\m -> m ! "blue" <= 14 && m ! "red" <= 12 && m ! "green" <= 13) (snd x)) (zip [1..] games)))
            part2 = sum(map(\game -> product (elems (foldl(\l r -> fromList[("blue", max(l ! "blue")( r ! "blue")),("red", max(l ! "red")( r ! "red")),("green", max(l ! "green")( r ! "green"))]) emptyMap game))) games)
        print part1
        print part2
        hClose handle

parse :: [String] -> [Map String Int]
parse("Game":_:x) = parse x
parse (no:colour:x) = do
                        let col = filter isLetter colour :: String
                        let res = parse x
                        case last colour of ',' -> insert col (read no)  (head res) : tail res
                                            ';' -> insert col (read no :: Int)  emptyMap : res
                                            isLetter -> [insert colour (read no :: Int) emptyMap]
                                            otherwise -> error (no ++ colour ++ (show x))
parse [] = []
parse x = error (show x)

emptyMap = fromList[("blue",0),("red",0),("green",0)] :: Map String Int
