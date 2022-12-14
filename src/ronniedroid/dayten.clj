(ns ronniedroid.dayten
  (:gen-class)
  (:require [clojure.string :as str]))

(def input (slurp "resources/dayteninput.txt"))

(def parsed-input
  (-> (str/split input #"\n")
      (->> (map #(str/split % #" ")))))

(def special-cycles [20 60 100 140 180 220])

(defn converted
  [coll]
  (reduce
   (fn [acc item]
     (let [fir (first item)
           sec (if (= fir "addx")
                 (read-string (second item))
                 0)]
       (if (= fir "noop")
         (conj acc sec)
         (conj acc 0 sec))))
   [1]
   coll))

(defn count-cycles
  [coll cycle]
  (* cycle
     (reduce + (take cycle (converted coll)))))

(def cycles
  (reduce
   (fn [acc item]
     (conj acc (count-cycles parsed-input item)))
   []
   special-cycles))

(reduce + cycles)

(def xvalues
  (first (reduce (fn [[p o] n] [(conj p (+ n o))
                                (+ n o)])
                 [[] 0]
                 (converted parsed-input))))

(def rows (flatten (repeat 6 (range 40))))

(defn has?
  [coll pred]
  (not= (some #{pred} coll) nil))

(def screen
  (let [values
        (reduce
         (fn [acc item]
           (let [a (first item)
                 b (second item)]
             (conj acc (- b a))))
         []
         (partition 2 (interleave rows xvalues)))]
    (map #(if (has? [-1 0 1] %) "☐" " ") values)))

(prn (map #(str/join %) (partition 40 screen)))

'("☐☐☐    ☐☐ ☐☐☐☐ ☐☐☐  ☐  ☐ ☐☐☐  ☐☐☐☐ ☐  ☐ "
  "☐  ☐    ☐ ☐    ☐  ☐ ☐  ☐ ☐  ☐ ☐    ☐  ☐ "
  "☐☐☐     ☐ ☐☐☐  ☐  ☐ ☐☐☐☐ ☐  ☐ ☐☐☐  ☐  ☐ "
  "☐  ☐    ☐ ☐    ☐☐☐  ☐  ☐ ☐☐☐  ☐    ☐  ☐ "
  "☐  ☐ ☐  ☐ ☐    ☐ ☐  ☐  ☐ ☐ ☐  ☐    ☐  ☐ "
  "☐☐☐   ☐☐  ☐    ☐  ☐ ☐  ☐ ☐  ☐ ☐     ☐☐  ")
