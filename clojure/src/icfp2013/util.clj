(ns icfp2013.util)

(defn to-hex
  "Returns the number x as hex string."
  [x]
  (str "0x" (format "%016X" x)))

(defn to-bin
  "Returns the number x as binary string."
  [x]
  (apply str (map #(if (= \space %1) \0 %1) (format "%64s" (Long/toBinaryString x)))))

(defn from-hex
  "Parses a number from a hex string."
  [s]
  (unchecked-long (read-string s)))

(defn gen-inputs
  "Generates 256 input vectors."
  []
  (let [one-bit-all-pos (->> (range 0 64) (map (partial bit-shift-left 1)))
        two-bits-all-pos (->> (range 0 63) (map (partial bit-shift-left 2r11)))
        three-bits-all-pos (->> (range 0 62) (map (partial bit-shift-left 2r111)))
        four-bits-all-pos (->> (range 0 61) (map (partial bit-shift-left 2r1111)))
        one-zero-one-zero (reduce #(bit-or %1 (bit-shift-left 2r10 (* 2 %2))) 2r10 (range 1 32))
        zero-one-zero-one (reduce #(bit-or %1 (bit-shift-left 2r01 (* 2 %2))) 2r01 (range 1 32))]
    (concat
      one-bit-all-pos
      two-bits-all-pos
      three-bits-all-pos
      four-bits-all-pos
      [0 5 9 -1 one-zero-one-zero zero-one-zero-one])))
