module Day8 where
import Lib
import Data.Map(Map, fromList)
import Data.List.Split(oneOf, split, dropDelims, splitOneOf)
import System.IO

main = do 
        handle <- getInput 8
        content <- hGetContents handle
        let lines' = lines content
            insts = head lines' ++ insts
            nodes = fromList $ map parseLine $ drop 2 lines'
        print nodes
        hClose handle

parseLine :: String -> (String,(String,String))
parseLine l = let [p,l,r] = splitOneOf "-, ()" l
              in (p, (l, r))  