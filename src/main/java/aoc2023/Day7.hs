module Day7
where 
import System.IO
import Lib
import Data.Map(Map, fromList, elems , fromListWith, notMember, (!), adjust, delete, toList)
import Data.List(sortOn, sortBy)
import Data.Ord(Down(..), comparing)
import Data.Char(ord)
import Control.Exception

main :: IO()
main = do
        handle <- getInput 7
        content <- hGetContents handle
        let parsed = map parseLine $ lines content
            sorted = sortBy(comparing (status . fst) <> comparing fst) parsed
            sorted2 = sortBy(comparing(status2 . fst) <> comparing fst) $ map (\(h,r) -> (map (\a -> if a == 11 then 0 else a) h, r))parsed
            part1 = sum $ map (\(i, (_, rank)) -> i * rank) $ zip [1..] sorted
            part2 = sum $ map (\(i, (_, rank)) -> i * rank) $ zip [1..] sorted2
        print part1
        print part2


parseLine :: String -> ([Int], Int)
parseLine l = let [k,v] = words l 
              in (map cardStrength k, read v)

cardStrength :: Char -> Int
cardStrength 'A' = 14
cardStrength 'K' = 13
cardStrength 'Q' = 12
cardStrength 'J' = 11
cardStrength 'T' = 10
cardStrength d = ord d - 48

status2 :: [Int] -> [Int]
status2 [0,0,0,0,0] = [5]
status2 hand = let normal = Data.Map.fromListWith (+) . map (\x -> (x,1)) $ hand 
               in (sortOn Down) . elems $ if notMember 0 normal then normal
               else let js = normal ! 0
                        newMap = delete 0 normal
                        (k,v) = head (sortBy(comparing $ negate . snd) . toList $ newMap)
                    in adjust (js + ) k newMap                

status :: [Int] -> [Int]
status hand = (sortOn Down) . Data.Map.elems . Data.Map.fromListWith (+) . map (\x -> (x,1)) $ hand               
