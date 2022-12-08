(ns ronniedroid.daytwo
  (:gen-class)
  (:require [clojure.string :as str]))

(def input (slurp "resources/daytwoinput.txt"))

(def parsed-input
  (-> input
      (str/split #"")
      (->> (filter #(not (str/blank? %))))))

(defn char-to-int
  [char]
  (case char
    "A" 1
    "X" 1
    "B" 2
    "Y" 2
    "C" 3
    "Z" 3))

(defn part-one
  [coll]
  (->>
   coll
   (map #(char-to-int %))
   (partition 2)
   (map
    #(let [fir (first %)
           sec (second %)]
       (cond (and (= fir 1) (= sec 2)) (+ sec 6)
             (and (= fir 2) (= sec 1)) (+ sec 0)
             (and (= fir 1) (= sec 3)) (+ sec 0)
             (and (= fir 3) (= sec 1)) (+ sec 6)
             (and (= fir 3) (= sec 2)) (+ sec 0)
             (and (= fir 2) (= sec 3)) (+ sec 6)
             (= fir sec) (+ sec 3))))
   (apply +)))

(defn part-two
  [coll]
  (->>
   coll
   (partition 2)
   (map
    #(let [fir (first %)
           sec (second %)]
       (cond (= sec "X") (cond (= fir "A") 3
                               (= fir "B") 1
                               (= fir "C") 2)
             (= sec "Y") (cond (= fir "A") 4
                               (= fir "B") 5
                               (= fir "C") 6)
             (= sec "Z") (cond (= fir "A") 8
                               (= fir "B") 9
                               (= fir "C") 7))))
   (apply +)))
