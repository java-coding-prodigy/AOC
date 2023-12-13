module Lib(getInput) where
import System.IO

getInput :: Int -> IO(Handle)
getInput n = openFile("src/main/resources/2023/Day" ++ (show n) ++ ".txt") ReadMode

