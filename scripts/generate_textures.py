#!/usr/bin/env python3
"""
Generate Minecraft 16x16 pixel art textures using ComfyUI + Flux Schnell.
Generates at 256x256 then downscales with nearest-neighbor for pixel art look.

Usage:
    python3 generate_textures.py              # Generate all textures
    python3 generate_textures.py --item riptide  # Generate specific item
    python3 generate_textures.py --list       # List all items
"""

import argparse
import json
import os
import random
import sys
import time
import urllib.request
import urllib.error
from io import BytesIO

COMFYUI_URL = os.environ.get("COMFYUI_URL", "http://localhost:8188")
OUTPUT_DIR = os.path.join(os.path.dirname(os.path.dirname(__file__)),
                          "src/main/resources/assets/demigodsfate/textures/item")
BLOCK_OUTPUT_DIR = os.path.join(os.path.dirname(os.path.dirname(__file__)),
                                "src/main/resources/assets/demigodsfate/textures/block")

# Item texture definitions: name -> prompt
ITEM_TEXTURES = {
    # Celestial Bronze tools
    "celestial_bronze_ingot": "a single bronze metal ingot bar, warm copper-bronze color, slightly glowing, simple flat minecraft game icon style, solid background, 2d pixel art, top-down view",
    "celestial_bronze_sword": "a bronze short sword weapon, copper-gold blade with leather wrapped handle, minecraft game item icon, pixel art style, transparent background, simple clean design",
    "celestial_bronze_axe": "a bronze battle axe, copper-gold axe head with wooden handle, minecraft game item icon, pixel art style, simple clean design",
    "celestial_bronze_pickaxe": "a bronze pickaxe mining tool, copper-gold pick head with wooden handle, minecraft game item, pixel art style, simple clean",
    "celestial_bronze_shovel": "a bronze shovel digging tool, copper-gold spade with wooden handle, minecraft game item, pixel art style, simple clean",
    "celestial_bronze_hoe": "a bronze farming hoe, copper-gold blade with wooden handle, minecraft game item, pixel art style, simple clean",

    # Imperial Gold tools
    "imperial_gold_ingot": "a single pure gold ingot bar, bright shining gold, ornate roman style, minecraft game icon, pixel art style, solid background",
    "imperial_gold_sword": "a golden roman gladius sword, bright gold blade with red leather grip, ornate, minecraft game item icon, pixel art style, simple design",
    "imperial_gold_axe": "a golden roman battle axe, bright gold axe head, ornate, minecraft game item icon, pixel art style",
    "imperial_gold_pickaxe": "a golden pickaxe, bright gold pick head with wooden handle, minecraft game item, pixel art style",
    "imperial_gold_shovel": "a golden shovel, bright gold spade with wooden handle, minecraft game item, pixel art style",
    "imperial_gold_hoe": "a golden farming hoe, bright gold blade with wooden handle, minecraft game item, pixel art style",

    # Stygian Iron
    "stygian_iron_ingot": "a dark iron ingot bar, very dark gray almost black with purple shimmer, shadowy, minecraft game icon, pixel art style, solid background",
    "stygian_iron_sword": "a dark shadowy sword, black iron blade with purple glow, sinister design, minecraft game item icon, pixel art style",

    # Divine weapons
    "riptide": "a magical glowing blue-green bronze sword, ocean water energy swirling around blade, celestial weapon, minecraft game item icon, pixel art style, magical glow",
    "ivlivs": "a golden roman coin sword, one side coin one side blade, imperial gold, glowing, minecraft game item icon, pixel art style",
    "aegis_shield": "a round silver greek shield with medusa face emblem, terrifying, ancient greek, minecraft game item icon, pixel art style",
    "backbiter": "a cursed sword half bronze half steel, dark red aura, sinister double-material blade, minecraft game item icon, pixel art style",
    "forge_hammer": "a glowing orange-bronze blacksmith hammer, fire embers around it, hephaestus forge tool, minecraft game item icon, pixel art style",
    "katoptris": "an elegant purple-tinted ceremonial dagger, ancient greek sacrificial knife, shimmering, minecraft game item icon, pixel art style",
    "morphing_spear": "a dark red roman spear with shapeshifting aura, imperial gold tip, magical weapon, minecraft game item icon, pixel art style",

    # Consumables
    "ambrosia": "a golden square of divine food, glowing warm golden light, heavenly dessert, minecraft game food item, pixel art style",
    "nectar": "an orange-golden potion bottle, divine drink, warm glowing liquid, minecraft game potion item, pixel art style",
    "golden_drachma": "a single ancient greek gold coin, owl emblem, round shining coin, minecraft game item icon, pixel art style",
    "greek_fire": "a green fire flask bottle, green glowing volatile liquid, dangerous alchemical fire, minecraft game throwable item, pixel art style",
    "hermes_multivitamin": "a small green medicine bottle with winged cap, hermes caduceus symbol, healing potion, minecraft game item, pixel art style",

    # Monster drops
    "minotaur_horn": "a large curved brown bull horn, trophy item, beast horn, minecraft game item, pixel art style",
    "hellhound_fang": "a sharp black dog fang tooth, dark shadowy, demonic canine tooth, minecraft game item, pixel art style",
    "dracanae_scale": "a green reptilian scale, snake woman armor scale, iridescent green, minecraft game item, pixel art style",
    "cyclops_eye": "a large single blue eye, glowing cyclops eye, glass-like orb, minecraft game item, pixel art style",
    "fury_whip_fragment": "a piece of burning orange leather whip, fire whip fragment, still smoldering, minecraft game item, pixel art style",
}

BLOCK_TEXTURES = {
    "celestial_bronze_block": "a tiled bronze metal block texture, warm copper-bronze, slightly ornate, minecraft block texture, seamless tileable, flat 2d",
    "imperial_gold_block": "a tiled gold metal block texture, bright shining gold, roman ornate pattern, minecraft block texture, seamless tileable, flat 2d",
    "oracle_seat": "a purple mystical stone throne texture, ancient greek oracle seat, purple stone with gold veins, minecraft block texture, seamless tileable",
}


def make_flux_workflow(prompt, seed, width=256, height=256, filename_prefix="texture"):
    """Build a Flux Schnell workflow for ComfyUI."""
    return {
        "6": {
            "class_type": "CLIPTextEncode",
            "inputs": {
                "text": prompt,
                "clip": ["11", 0],
            }
        },
        "8": {
            "class_type": "VAEDecode",
            "inputs": {
                "samples": ["13", 0],
                "vae": ["10", 0],
            }
        },
        "9": {
            "class_type": "SaveImage",
            "inputs": {
                "filename_prefix": filename_prefix,
                "images": ["8", 0],
            }
        },
        "10": {
            "class_type": "VAELoader",
            "inputs": {
                "vae_name": "ae.safetensors"
            }
        },
        "11": {
            "class_type": "DualCLIPLoader",
            "inputs": {
                "clip_name1": "t5xxl_fp8_e4m3fn.safetensors",
                "clip_name2": "clip_l.safetensors",
                "type": "flux"
            }
        },
        "13": {
            "class_type": "KSampler",
            "inputs": {
                "seed": seed,
                "steps": 4,
                "cfg": 1.0,
                "sampler_name": "euler",
                "scheduler": "simple",
                "denoise": 1.0,
                "model": ["12", 0],
                "positive": ["6", 0],
                "negative": ["6", 0],
                "latent_image": ["5", 0],
            }
        },
        "12": {
            "class_type": "UNETLoader",
            "inputs": {
                "unet_name": "flux1-schnell.safetensors",
                "weight_dtype": "fp8_e4m3fn"
            }
        },
        "5": {
            "class_type": "EmptyLatentImage",
            "inputs": {
                "width": width,
                "height": height,
                "batch_size": 1,
            }
        },
    }


def make_sdxl_workflow(prompt, negative, seed, width=256, height=256, filename_prefix="texture"):
    """Build an SDXL workflow using Illustrious XL."""
    return {
        "3": {
            "class_type": "KSampler",
            "inputs": {
                "seed": seed,
                "steps": 20,
                "cfg": 7.0,
                "sampler_name": "euler_ancestral",
                "scheduler": "normal",
                "denoise": 1.0,
                "model": ["4", 0],
                "positive": ["6", 0],
                "negative": ["7", 0],
                "latent_image": ["5", 0],
            }
        },
        "4": {
            "class_type": "CheckpointLoaderSimple",
            "inputs": {
                "ckpt_name": "Illustrious-XL-v0.1.safetensors"
            }
        },
        "5": {
            "class_type": "EmptyLatentImage",
            "inputs": {
                "width": width,
                "height": height,
                "batch_size": 1,
            }
        },
        "6": {
            "class_type": "CLIPTextEncode",
            "inputs": {
                "text": prompt,
                "clip": ["4", 1],
            }
        },
        "7": {
            "class_type": "CLIPTextEncode",
            "inputs": {
                "text": negative,
                "clip": ["4", 1],
            }
        },
        "8": {
            "class_type": "VAEDecode",
            "inputs": {
                "samples": ["3", 0],
                "vae": ["4", 2],
            }
        },
        "9": {
            "class_type": "SaveImage",
            "inputs": {
                "filename_prefix": filename_prefix,
                "images": ["8", 0],
            }
        },
    }


def queue_prompt(workflow):
    """Send a workflow to ComfyUI and return the prompt_id."""
    data = json.dumps({"prompt": workflow}).encode("utf-8")
    req = urllib.request.Request(f"{COMFYUI_URL}/prompt",
                                data=data,
                                headers={"Content-Type": "application/json"})
    resp = urllib.request.urlopen(req)
    return json.loads(resp.read())["prompt_id"]


def wait_for_completion(prompt_id, timeout=120):
    """Poll ComfyUI history until the prompt is done."""
    start = time.time()
    while time.time() - start < timeout:
        try:
            resp = urllib.request.urlopen(f"{COMFYUI_URL}/history/{prompt_id}")
            history = json.loads(resp.read())
            if prompt_id in history:
                return history[prompt_id]
        except Exception:
            pass
        time.sleep(1)
    raise TimeoutError(f"Prompt {prompt_id} didn't complete in {timeout}s")


def get_image(filename, subfolder, folder_type="output"):
    """Download a generated image from ComfyUI."""
    url = f"{COMFYUI_URL}/view?filename={filename}&subfolder={subfolder}&type={folder_type}"
    resp = urllib.request.urlopen(url)
    return resp.read()


def downscale_to_16x16(image_data):
    """Downscale image data to 16x16 using nearest-neighbor (pixel art style)."""
    # Use PIL if available, otherwise use a simple approach
    try:
        from PIL import Image
        img = Image.open(BytesIO(image_data))
        img = img.resize((16, 16), Image.NEAREST)
        output = BytesIO()
        img.save(output, format="PNG")
        return output.getvalue()
    except ImportError:
        print("WARNING: PIL not available, saving at original size")
        return image_data


def generate_texture(name, prompt, output_dir, use_sdxl=False):
    """Generate a single texture."""
    seed = random.randint(0, 2**32)
    prefix = f"mc_{name}"

    if use_sdxl:
        negative = "blurry, photorealistic, 3d render, photograph, complex background, text, watermark, signature"
        workflow = make_sdxl_workflow(prompt, negative, seed, 256, 256, prefix)
    else:
        workflow = make_flux_workflow(prompt, seed, 256, 256, prefix)

    print(f"  Generating {name}...", end=" ", flush=True)
    try:
        prompt_id = queue_prompt(workflow)
        result = wait_for_completion(prompt_id, timeout=120)

        # Find the output image
        outputs = result.get("outputs", {})
        for node_id, node_output in outputs.items():
            if "images" in node_output:
                for img_info in node_output["images"]:
                    image_data = get_image(img_info["filename"], img_info["subfolder"])
                    # Downscale to 16x16
                    pixel_art = downscale_to_16x16(image_data)
                    # Save
                    output_path = os.path.join(output_dir, f"{name}.png")
                    with open(output_path, "wb") as f:
                        f.write(pixel_art)
                    print(f"OK ({len(pixel_art)} bytes)")
                    return True

        print("FAIL (no output images)")
        return False
    except Exception as e:
        print(f"FAIL ({e})")
        return False


def main():
    parser = argparse.ArgumentParser(description="Generate Minecraft textures via ComfyUI")
    parser.add_argument("--item", help="Generate specific item only")
    parser.add_argument("--list", action="store_true", help="List all items")
    parser.add_argument("--sdxl", action="store_true", help="Use SDXL instead of Flux")
    parser.add_argument("--blocks", action="store_true", help="Generate block textures")
    args = parser.parse_args()

    if args.list:
        print("Item textures:")
        for name in ITEM_TEXTURES:
            print(f"  {name}")
        print(f"\nBlock textures:")
        for name in BLOCK_TEXTURES:
            print(f"  {name}")
        return

    os.makedirs(OUTPUT_DIR, exist_ok=True)
    os.makedirs(BLOCK_OUTPUT_DIR, exist_ok=True)

    # Check ComfyUI is running
    try:
        urllib.request.urlopen(f"{COMFYUI_URL}/system_stats")
    except Exception:
        print(f"ERROR: ComfyUI not running at {COMFYUI_URL}")
        sys.exit(1)

    if args.item:
        if args.item in ITEM_TEXTURES:
            generate_texture(args.item, ITEM_TEXTURES[args.item], OUTPUT_DIR, args.sdxl)
        elif args.item in BLOCK_TEXTURES:
            generate_texture(args.item, BLOCK_TEXTURES[args.item], BLOCK_OUTPUT_DIR, args.sdxl)
        else:
            print(f"Unknown item: {args.item}")
        return

    textures = ITEM_TEXTURES
    out_dir = OUTPUT_DIR
    if args.blocks:
        textures = BLOCK_TEXTURES
        out_dir = BLOCK_OUTPUT_DIR

    total = len(textures)
    success = 0
    print(f"Generating {total} textures using {'SDXL' if args.sdxl else 'Flux Schnell'}...")
    print()

    for i, (name, prompt) in enumerate(textures.items(), 1):
        print(f"[{i}/{total}]", end=" ")
        if generate_texture(name, prompt, out_dir, args.sdxl):
            success += 1

    print(f"\nDone! {success}/{total} textures generated.")


if __name__ == "__main__":
    main()
