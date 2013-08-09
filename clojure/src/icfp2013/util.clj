(ns icfp2013.util)

(defn to-hex
  "Returns the number x as hex string."
  [x]
  (str "0x" (format "%016X" x)))

(defn from-hex
  "Parses a number from a hex string."
  [s]
  (unchecked-long (read-string s)))
