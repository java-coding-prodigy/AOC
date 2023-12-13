module Day6(main)
where
import System.IO
import Lib
import Data.Char(isDigit)

main = do
        handle <- getInput 6
        contents <- hGetContents handle
        let lines' = lines contents
        let [t,d] = map (map read . tail . words) $ lines'
            part1 = product (map betterScores $ zip t d)
            [tf,df] = map (read . filter isDigit) lines'  
            part2 = betterScores(tf , df)
        print (tf, df)   
        print part1   
        print part2     
        hClose handle

betterScores :: (Double, Double) -> Int
betterScores (time, dist) = let disc = sqrt(time*time - 4 * dist) / 2.0
                                negBby2 = time / 2.0
                                x1 = negBby2 - disc
                                x2 = negBby2 + disc
                                ceilX1 = ceiling x1
                                floorX2 = floor x2
                           
                            in 1 + (if(fromIntegral floorX2 == x2) then floorX2 - 1 else floorX2 ) - (if(fromIntegral ceilX1 == x1) then ceilX1 + 1 else ceilX1)   
                             