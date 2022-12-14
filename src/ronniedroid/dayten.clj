(ns ronniedroid.dayten
  (:gen-class)
  (:require [clojure.string :as str]))

(def input (slurp "resources/dayteninput.txt"))

(defn has?
  [coll pred]
  (not= (some #{pred} coll) nil))

(def parsed-input
  (->> (str/split input #"\n")
       (map #(str/split % #" "))
       (reduce
        (fn [acc item]
          (let [fir (first item)
                sec (if (= fir "addx")
                      (read-string (second item))
                      0)]
            (if (= fir "noop")
              (conj acc sec)
              (conj acc 0 sec))))
        [1])))

(def special-cycles
  (->>
   (reduce
    (fn [acc cycle]
      (conj
       acc
       (* cycle
          (reduce + (take cycle parsed-input)))))
    []
    [20 60 100 140 180 220])
   (reduce +)))

(def screen
  (->> (reduce (fn [[p o] n] [(conj p (+ n o))
                              (+ n o)])
               [[] 0]
               parsed-input)
       first
       (interleave (flatten (repeat 6
                                    (range 40))))
       (partition 2)
       (map #(reduce - %))
       (map #(if (has? [-1 0 1] %) "☐" " "))
       (partition 40)
       (map #(str/join %))))
