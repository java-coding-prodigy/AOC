module Day5(main) where
import Lib
import System.IO
import Data.List.Split(split, oneOf, dropDelims)
import Data.Map(Map, fromList, (!), keys)
import Debug.Trace

main = do
        handle <- getInput 5
        string <- hGetContents handle
        let maps = split (dropDelims $ oneOf [""]) (lines string)
        --print maps
        let seeds = map read (tail(words(head (head maps)))) :: [Int]
            parsed = fromList(map parseGroup (tail maps))
        print maps     
        print (keys parsed)    
        let locations = delve seeds "seed" parsed
            part1 = minimum locations    
        print seeds
        print part1
        hClose handle    

parseGroup :: [String] -> (String, (String, Int -> Int))
parseGroup (mapName: remaining) = let (k:_:v:rest) = trace(show(split(dropDelims $ oneOf "- :") mapName)) split(dropDelims $ oneOf "- : ") mapName
                                      ints = map (\w -> map read (words w)) remaining
                                  in (trace(k ++ "-to" ++ v) k, (v,  getFunction ints))
getFunction :: [[Int]] -> (Int -> Int)
getFunction ((dest: src: length:rest): tail) =  let f :: Int -> Int
                                                    f n 
                                                     | src <= n && n < src + length = dest + n - src
                                                     | null tail = n
                                                     | otherwise = getFunction tail n
                                                in f  



delve :: [Int] -> String -> Map String (String, Int -> Int) -> [Int]
delve nums trait stuff
        | trait == "location" = nums
        | otherwise = delve (map f nums) next stuff
                      where (next, f) = stuff ! trait
