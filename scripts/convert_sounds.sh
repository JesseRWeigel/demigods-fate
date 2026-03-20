#!/bin/bash
# Convert WAV sound files to OGG (Minecraft format) and organize into subdirectories
SOUNDS_DIR="src/main/resources/assets/demigodsfate/sounds"

mkdir -p "$SOUNDS_DIR/music" "$SOUNDS_DIR/effect"

echo "Converting sounds to OGG format..."

for wav in "$SOUNDS_DIR"/*.wav; do
    [ -f "$wav" ] || continue
    basename=$(basename "$wav" .wav)

    # Determine if it's music or effect based on name
    if echo "$basename" | grep -qE "ambient|theme|battle"; then
        outdir="$SOUNDS_DIR/music"
    else
        outdir="$SOUNDS_DIR/effect"
    fi

    ogg="$outdir/${basename}.ogg"
    echo "  $basename.wav → $ogg"
    ffmpeg -y -i "$wav" -acodec libvorbis -q:a 4 "$ogg" 2>/dev/null
    rm "$wav"  # Remove WAV after conversion
done

echo "Done! OGG files:"
find "$SOUNDS_DIR" -name "*.ogg" -exec ls -lh {} \;
