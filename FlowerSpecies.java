/*     */ package binnie.botany.genetics;
/*     */ import binnie.botany.api.EnumAcidity;
/*     */ import binnie.botany.api.EnumFlowerChromosome;
/*     */ import binnie.botany.api.EnumMoisture;
/*     */ import binnie.botany.api.IFlowerRoot;
/*     */ import binnie.botany.api.IFlowerType;
/*     */ import binnie.botany.core.BotanyCore;
/*     */ import binnie.genetics.genetics.AlleleHelper;
/*     */ import forestry.api.core.EnumHumidity;
/*     */ import forestry.api.core.EnumTemperature;
/*     */ import forestry.api.genetics.AlleleManager;
/*     */ import forestry.api.genetics.EnumTolerance;
/*     */ import forestry.api.genetics.IAllele;
/*     */ import forestry.api.genetics.IClassification;
/*     */ import forestry.api.genetics.ISpeciesRoot;
/*     */ import forestry.core.genetics.alleles.AlleleHelper;
/*     */ import forestry.core.genetics.alleles.EnumAllele;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ 
/*     */ public enum FlowerSpecies implements IAlleleFlowerSpecies {
/*  24 */   DANDELION("dandelion", "taraxacum", "officinale", EnumFlowerType.DANDELION, EnumFlowerColor.YELLOW),
/*  25 */   POPPY("poppy", "papaver", "rhoeas", EnumFlowerType.POPPY, EnumFlowerColor.RED),
/*  26 */   ORCHID("orchid", "vanda", "coerulea", EnumFlowerType.ORCHID, EnumFlowerColor.DEEP_SKY_BLUE),
/*  27 */   ALLIUM("allium", "allium", "giganteum", EnumFlowerType.ALLIUM, EnumFlowerColor.MEDIUM_PURPLE),
/*  28 */   BLUET("bluet", "houstonia", "caerulea", EnumFlowerType.BLUET, EnumFlowerColor.LAVENDER, EnumFlowerColor.KHAKI),
/*  29 */   TULIP("tulip", "tulipa", "agenensis", EnumFlowerType.TULIP, EnumFlowerColor.VIOLET),
/*  30 */   DAISY("daisy", "leucanthemum", "vulgare", EnumFlowerType.DAISY, EnumFlowerColor.WHITE, EnumFlowerColor.YELLOW),
/*  31 */   CORNFLOWER("cornflower", "centaurea", "cyanus", EnumFlowerType.CORNFLOWER, EnumFlowerColor.SKY_BLUE),
/*  32 */   PANSY("pansy", "viola", "tricolor", EnumFlowerType.PANSY, EnumFlowerColor.PINK, EnumFlowerColor.PURPLE),
/*  33 */   IRIS("iris", "iris", "germanica", EnumFlowerType.IRIS, EnumFlowerColor.LIGHT_GRAY, EnumFlowerColor.PURPLE),
/*  34 */   LAVENDER("lavender", "Lavandula", "angustifolia", EnumFlowerType.LAVENDER, EnumFlowerColor.MEDIUM_ORCHID),
/*  35 */   VIOLA("viola", "viola", "odorata", EnumFlowerType.VIOLA, EnumFlowerColor.MEDIUM_PURPLE, EnumFlowerColor.SLATE_BLUE),
/*  36 */   DAFFODIL("daffodil", "narcissus", "pseudonarcissus", EnumFlowerType.DAFFODIL, EnumFlowerColor.YELLOW, EnumFlowerColor.GOLD),
/*  37 */   DAHLIA("dahlia", "dahlia", "variabilis", EnumFlowerType.DAHLIA, EnumFlowerColor.HOT_PINK, EnumFlowerColor.DEEP_PINK),
/*  38 */   PEONY("peony", "paeonia", "suffruticosa", EnumFlowerType.PEONY, EnumFlowerColor.THISTLE),
/*  39 */   ROSE("rose", "rosa", "rubiginosa", EnumFlowerType.ROSE, EnumFlowerColor.RED),
/*  40 */   LILAC("lilac", "syringa", "vulgaris", EnumFlowerType.LILAC, EnumFlowerColor.PLUM),
/*  41 */   HYDRANGEA("hydrangea", "hydrangea", "macrophylla", EnumFlowerType.HYDRANGEA, EnumFlowerColor.DEEP_SKY_BLUE),
/*  42 */   FOXGLOVE("foxglove", "digitalis", "purpurea", EnumFlowerType.FOXGLOVE, EnumFlowerColor.HOT_PINK),
/*  43 */   ZINNIA("zinnia", "zinnia", "elegans", EnumFlowerType.ZINNIA, EnumFlowerColor.MEDIUM_VIOLET_RED, EnumFlowerColor.YELLOW),
/*  44 */   CHRYSANTHEMUM("chrysanthemum", "chrysanthemum", "ï?? grandiflorum", EnumFlowerType.MUMS, EnumFlowerColor.VIOLET),
/*  45 */   MARIGOLD("marigold", "calendula", "officinalis", EnumFlowerType.MARIGOLD, EnumFlowerColor.GOLD, EnumFlowerColor.DARK_ORANGE),
/*  46 */   GERANIUM("geranium", "geranium", "maderense", EnumFlowerType.GERANIUM, EnumFlowerColor.DEEP_PINK),
/*  47 */   AZALEA("azalea", "rhododendrons", "aurigeranum", EnumFlowerType.AZALEA, EnumFlowerColor.HOT_PINK),
/*  48 */   PRIMROSE("primrose", "primula", "vulgaris", EnumFlowerType.PRIMROSE, EnumFlowerColor.RED, EnumFlowerColor.GOLD),
/*  49 */   ASTER("aster", "aster", "amellus", EnumFlowerType.ASTER, EnumFlowerColor.MEDIUM_PURPLE, EnumFlowerColor.GOLDENROD),
/*  50 */   CARNATION("carnation", "dianthus", "caryophyllus", EnumFlowerType.CARNATION, EnumFlowerColor.CRIMSON, EnumFlowerColor.WHITE),
/*  51 */   LILY("lily", "lilium", "auratum", EnumFlowerType.LILY, EnumFlowerColor.PINK, EnumFlowerColor.GOLD),
/*  52 */   YARROW("yarrow", "achillea", "millefolium", EnumFlowerType.YARROW, EnumFlowerColor.YELLOW),
/*  53 */   PETUNIA("petunia", "petunia", "ï?? atkinsiana", EnumFlowerType.PETUNIA, EnumFlowerColor.MEDIUM_VIOLET_RED, EnumFlowerColor.THISTLE),
/*  54 */   AGAPANTHUS("agapanthus", "agapanthus", "praecox", EnumFlowerType.AGAPANTHUS, EnumFlowerColor.DEEP_SKY_BLUE),
/*  55 */   FUCHSIA("fuchsia", "fuchsia", "magellanica", EnumFlowerType.FUCHSIA, EnumFlowerColor.DEEP_PINK, EnumFlowerColor.MEDIUM_ORCHID),
/*  56 */   DIANTHUS("dianthus", "dianthus", "barbatus", EnumFlowerType.DIANTHUS, EnumFlowerColor.CRIMSON, EnumFlowerColor.HOT_PINK),
/*  57 */   FORGET("forget-me-nots", "myosotis", "arvensis", EnumFlowerType.FORGET, EnumFlowerColor.LIGHT_STEEL_BLUE),
/*  58 */   ANEMONE("anemone", "anemone", "coronaria", EnumFlowerType.ANEMONE, EnumFlowerColor.RED, EnumFlowerColor.MISTY_ROSE),
/*  59 */   AQUILEGIA("aquilegia", "aquilegia", "vulgaris", EnumFlowerType.AQUILEGIA, EnumFlowerColor.SLATE_BLUE, EnumFlowerColor.THISTLE),
/*  60 */   EDELWEISS("edelweiss", "leontopodium", "alpinum", EnumFlowerType.EDELWEISS, EnumFlowerColor.WHITE, EnumFlowerColor.KHAKI),
/*  61 */   SCABIOUS("scabious", "scabiosa", "columbaria", EnumFlowerType.SCABIOUS, EnumFlowerColor.ROYAL_BLUE),
/*  62 */   CONEFLOWER("coneflower", "echinacea", "purpurea", EnumFlowerType.CONEFLOWER, EnumFlowerColor.VIOLET, EnumFlowerColor.DARK_ORANGE),
/*  63 */   GAILLARDIA("gaillardia", "gaillardia", "aristata", EnumFlowerType.GAILLARDIA, EnumFlowerColor.DARK_ORANGE, EnumFlowerColor.YELLOW),
/*  64 */   AURICULA("auricula", "primula", "auricula", EnumFlowerType.AURICULA, EnumFlowerColor.RED, EnumFlowerColor.YELLOW),
/*  65 */   CAMELLIA("camellia", "camellia", "japonica", EnumFlowerType.CAMELLIA, EnumFlowerColor.CRIMSON),
/*  66 */   GOLDENROD("goldenrod", "solidago", "canadensis", EnumFlowerType.GOLDENROD, EnumFlowerColor.GOLD),
/*  67 */   ALTHEA("althea", "althaea", "officinalis", EnumFlowerType.ALTHEA, EnumFlowerColor.THISTLE, EnumFlowerColor.MEDIUM_ORCHID),
/*  68 */   PENSTEMON("penstemon", "penstemon", "digitalis", EnumFlowerType.PENSTEMON, EnumFlowerColor.MEDIUM_ORCHID, EnumFlowerColor.THISTLE),
/*  69 */   DELPHINIUM("delphinium", "delphinium", "staphisagria", EnumFlowerType.DELPHINIUM, EnumFlowerColor.DARK_SLATE_BLUE),
/*  70 */   HOLLYHOCK("hollyhock", "Alcea", "rosea", EnumFlowerType.HOLLYHOCK, EnumFlowerColor.BLACK, EnumFlowerColor.GOLD);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   private final String uid = "botany.flowers.species." + toString().toLowerCase();
/*  93 */   private final int id = IAllele.toId(this.uid); protected EnumFlowerColor stemColor; protected EnumAllele.Fertility fert; protected EnumAllele.Lifespan life; protected EnumAllele.Sappiness sap;
/*     */   protected IFlowerType type;
/*     */   protected String name;
/*     */   protected String binomial;
/*     */   protected String branchName;
/*     */   protected EnumFlowerColor primaryColor;
/*     */   
/*     */   FlowerSpecies(String name, String branch, String binomial, IFlowerType type, EnumFlowerColor primaryColor, EnumFlowerColor secondaryColor) {
/* 101 */     this.name = name;
/* 102 */     this.binomial = binomial;
/* 103 */     this.type = type;
/* 104 */     this.primaryColor = primaryColor;
/* 105 */     this.secondaryColor = secondaryColor;
/* 106 */     this.stemColor = EnumFlowerColor.GREEN;
/* 107 */     this.temperature = EnumTemperature.NORMAL;
/* 108 */     this.pH = EnumAcidity.NEUTRAL;
/* 109 */     this.moisture = EnumMoisture.NORMAL;
/* 110 */     this.tempTolerance = EnumTolerance.BOTH_1;
/* 111 */     this.pHTolerance = EnumTolerance.NONE;
/* 112 */     this.moistureTolerance = EnumTolerance.NONE;
/* 113 */     this.variantTemplates = (List)new ArrayList<>();
/* 114 */     this.branchName = branch;
/*     */   }
/*     */   protected EnumFlowerColor secondaryColor; protected EnumTemperature temperature; protected EnumAcidity pH; protected EnumMoisture moisture; protected EnumTolerance tempTolerance; protected EnumTolerance pHTolerance; protected EnumTolerance moistureTolerance; protected List<IAllele[]> variantTemplates; protected IClassification branch;
/*     */   public static void setupVariants() {
/* 118 */     DANDELION.setTraits(EnumAllele.Fertility.HIGH, EnumAllele.Lifespan.SHORTENED, EnumAllele.Sappiness.LOWER)
/* 119 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.BOTH_1)
/* 120 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.BOTH_1)
/* 121 */       .setStemColor(EnumFlowerColor.GREEN);
/*     */     
/* 123 */     POPPY.setTraits(EnumAllele.Fertility.HIGH, EnumAllele.Lifespan.SHORTER, EnumAllele.Sappiness.AVERAGE)
/* 124 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.DOWN_1)
/* 125 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.DOWN_1)
/* 126 */       .setTemperature(EnumTemperature.WARM, EnumTolerance.BOTH_2)
/* 127 */       .setStemColor(EnumFlowerColor.GREEN);
/*     */     
/* 129 */     ORCHID.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.LONG, EnumAllele.Sappiness.LOW)
/* 130 */       .setPH(EnumAcidity.ACID, EnumTolerance.NONE)
/* 131 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.BOTH_1)
/* 132 */       .setStemColor(EnumFlowerColor.GREEN);
/*     */     
/* 134 */     ALLIUM.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.NORMAL, EnumAllele.Sappiness.LOW)
/* 135 */       .setPH(EnumAcidity.ALKALINE, EnumTolerance.DOWN_1)
/* 136 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.DOWN_1);
/*     */     
/* 138 */     BLUET.setTraits(EnumAllele.Fertility.LOW, EnumAllele.Lifespan.NORMAL, EnumAllele.Sappiness.LOWER)
/* 139 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.DOWN_1)
/* 140 */       .setMoisture(EnumMoisture.DAMP, EnumTolerance.NONE)
/* 141 */       .setStemColor(EnumFlowerColor.OLIVE_DRAB);
/*     */     
/* 143 */     TULIP.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.LONG, EnumAllele.Sappiness.AVERAGE)
/* 144 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.BOTH_1)
/* 145 */       .setStemColor(EnumFlowerColor.OLIVE_DRAB);
/*     */     
/* 147 */     DAISY.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.NORMAL, EnumAllele.Sappiness.LOW)
/* 148 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.DOWN_1)
/* 149 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.DOWN_1)
/* 150 */       .setTemperature(EnumTemperature.WARM, EnumTolerance.BOTH_2)
/* 151 */       .setStemColor(EnumFlowerColor.OLIVE_DRAB);
/*     */     
/* 153 */     CORNFLOWER.setTraits(EnumAllele.Fertility.HIGH, EnumAllele.Lifespan.SHORTER, EnumAllele.Sappiness.LOW)
/* 154 */       .setMutation(DANDELION, TULIP, 10)
/* 155 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.BOTH_1)
/* 156 */       .setStemColor(EnumFlowerColor.OLIVE_DRAB);
/*     */     
/* 158 */     PANSY.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.SHORTENED, EnumAllele.Sappiness.AVERAGE)
/* 159 */       .setMutation(TULIP, VIOLA, 5)
/* 160 */       .setPH(EnumAcidity.ACID, EnumTolerance.NONE)
/* 161 */       .setTemperature(EnumTemperature.WARM, EnumTolerance.DOWN_1)
/* 162 */       .setStemColor(EnumFlowerColor.SEA_GREEN);
/*     */     
/* 164 */     IRIS.setTraits(EnumAllele.Fertility.HIGH, EnumAllele.Lifespan.NORMAL, EnumAllele.Sappiness.AVERAGE)
/* 165 */       .setMutation(ORCHID, VIOLA, 10)
/* 166 */       .setPH(EnumAcidity.ACID, EnumTolerance.NONE)
/* 167 */       .setTemperature(EnumTemperature.WARM, EnumTolerance.DOWN_1)
/* 168 */       .setStemColor(EnumFlowerColor.SEA_GREEN);
/*     */     
/* 170 */     LAVENDER.setTraits(EnumAllele.Fertility.HIGH, EnumAllele.Lifespan.NORMAL, EnumAllele.Sappiness.LOW)
/* 171 */       .setMutation(ALLIUM, VIOLA, 10)
/* 172 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.UP_1)
/* 173 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.DOWN_1)
/* 174 */       .setTemperature(EnumTemperature.WARM, EnumTolerance.DOWN_1)
/* 175 */       .setStemColor(EnumFlowerColor.GREEN);
/*     */     
/* 177 */     VIOLA.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.SHORTENED, EnumAllele.Sappiness.AVERAGE)
/* 178 */       .setMutation(ORCHID, POPPY, 15)
/* 179 */       .setPH(EnumAcidity.ACID, EnumTolerance.NONE)
/* 180 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.DOWN_1)
/* 181 */       .setStemColor(EnumFlowerColor.OLIVE_DRAB);
/*     */     
/* 183 */     DAFFODIL.setTraits(EnumAllele.Fertility.HIGH, EnumAllele.Lifespan.ELONGATED, EnumAllele.Sappiness.AVERAGE)
/* 184 */       .setMutation(DANDELION, POPPY, 10)
/* 185 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.DOWN_1)
/* 186 */       .setStemColor(EnumFlowerColor.GREEN);
/*     */     
/* 188 */     ASTER.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.NORMAL, EnumAllele.Sappiness.HIGHER)
/* 189 */       .setMutation(DAISY, TULIP, 10)
/* 190 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.BOTH_1)
/* 191 */       .setStemColor(EnumFlowerColor.GREEN);
/*     */     
/* 193 */     LILAC.setTraits(EnumAllele.Fertility.LOW, EnumAllele.Lifespan.LONGER, EnumAllele.Sappiness.AVERAGE)
/* 194 */       .setPH(EnumAcidity.ALKALINE, EnumTolerance.DOWN_1)
/* 195 */       .setStemColor(EnumFlowerColor.OLIVE_DRAB);
/*     */     
/* 197 */     ROSE.setTraits(EnumAllele.Fertility.LOW, EnumAllele.Lifespan.LONGER, EnumAllele.Sappiness.HIGH)
/* 198 */       .setPH(EnumAcidity.ACID, EnumTolerance.UP_1)
/* 199 */       .setStemColor(EnumFlowerColor.GREEN);
/*     */     
/* 201 */     PEONY.setTraits(EnumAllele.Fertility.LOW, EnumAllele.Lifespan.LONG, EnumAllele.Sappiness.AVERAGE)
/* 202 */       .setPH(EnumAcidity.ALKALINE, EnumTolerance.DOWN_1)
/* 203 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.UP_1)
/* 204 */       .setStemColor(EnumFlowerColor.DARK_GREEN);
/*     */     
/* 206 */     MARIGOLD.setTraits(EnumAllele.Fertility.HIGH, EnumAllele.Lifespan.SHORTER, EnumAllele.Sappiness.AVERAGE)
/* 207 */       .setMutation(DAISY, DANDELION, 10)
/* 208 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.DOWN_1)
/* 209 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.UP_1)
/* 210 */       .setTemperature(EnumTemperature.WARM, EnumTolerance.BOTH_2)
/* 211 */       .setStemColor(EnumFlowerColor.GREEN);
/*     */     
/* 213 */     HYDRANGEA.setTraits(EnumAllele.Fertility.LOW, EnumAllele.Lifespan.LONGER, EnumAllele.Sappiness.HIGH)
/* 214 */       .setMutation(PEONY, BLUET, 10)
/* 215 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.BOTH_1)
/* 216 */       .setMoisture(EnumMoisture.DAMP, EnumTolerance.NONE)
/* 217 */       .setStemColor(EnumFlowerColor.DARK_GREEN);
/*     */     
/* 219 */     FOXGLOVE.setTraits(EnumAllele.Fertility.LOW, EnumAllele.Lifespan.SHORTENED, EnumAllele.Sappiness.LOW)
/* 220 */       .setMutation(LILAC, ZINNIA, 5)
/* 221 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.DOWN_1)
/* 222 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.UP_1)
/* 223 */       .setStemColor(EnumFlowerColor.DARK_GREEN);
/*     */     
/* 225 */     DAHLIA.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.NORMAL, EnumAllele.Sappiness.AVERAGE)
/* 226 */       .setMutation(DAISY, ALLIUM, 15)
/* 227 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.DOWN_1)
/* 228 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.UP_1)
/* 229 */       .setTemperature(EnumTemperature.NORMAL, EnumTolerance.BOTH_2)
/* 230 */       .setStemColor(EnumFlowerColor.OLIVE_DRAB);
/*     */     
/* 232 */     CHRYSANTHEMUM.setTraits(EnumAllele.Fertility.HIGH, EnumAllele.Lifespan.LONG, EnumAllele.Sappiness.HIGH)
/* 233 */       .setMutation(GERANIUM, ROSE, 10)
/* 234 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.DOWN_1)
/* 235 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.DOWN_1)
/* 236 */       .setStemColor(EnumFlowerColor.MEDIUM_SEA_GREEN);
/*     */     
/* 238 */     CARNATION.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.NORMAL, EnumAllele.Sappiness.HIGH)
/* 239 */       .setMutation(DIANTHUS, ROSE, 5)
/* 240 */       .setPH(EnumAcidity.ALKALINE, EnumTolerance.DOWN_1)
/* 241 */       .setStemColor(EnumFlowerColor.SEA_GREEN);
/*     */     
/* 243 */     ZINNIA.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.SHORTER, EnumAllele.Sappiness.AVERAGE)
/* 244 */       .setMutation(DAHLIA, MARIGOLD, 5)
/* 245 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.BOTH_1)
/* 246 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.DOWN_1)
/* 247 */       .setTemperature(EnumTemperature.NORMAL, EnumTolerance.BOTH_2)
/* 248 */       .setStemColor(EnumFlowerColor.MEDIUM_SEA_GREEN);
/*     */     
/* 250 */     PRIMROSE.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.LONG, EnumAllele.Sappiness.AVERAGE)
/* 251 */       .setMutation(CHRYSANTHEMUM, AURICULA, 5)
/* 252 */       .setPH(EnumAcidity.ACID, EnumTolerance.UP_1)
/* 253 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.UP_1)
/* 254 */       .setStemColor(EnumFlowerColor.GREEN);
/*     */     
/* 256 */     AZALEA.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.NORMAL, EnumAllele.Sappiness.AVERAGE)
/* 257 */       .setMutation(ORCHID, GERANIUM, 5)
/* 258 */       .setPH(EnumAcidity.ACID, EnumTolerance.NONE)
/* 259 */       .setStemColor(EnumFlowerColor.GREEN);
/*     */     
/* 261 */     GERANIUM.setTraits(EnumAllele.Fertility.LOW, EnumAllele.Lifespan.LONG, EnumAllele.Sappiness.LOW)
/* 262 */       .setMutation(TULIP, ORCHID, 15)
/* 263 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.BOTH_1)
/* 264 */       .setTemperature(EnumTemperature.WARM, EnumTolerance.BOTH_1)
/* 265 */       .setStemColor(EnumFlowerColor.MEDIUM_SEA_GREEN);
/*     */     
/* 267 */     LILY.setTraits(EnumAllele.Fertility.LOW, EnumAllele.Lifespan.LONG, EnumAllele.Sappiness.LOW)
/* 268 */       .setMutation(TULIP, CHRYSANTHEMUM, 5)
/* 269 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.DOWN_1)
/* 270 */       .setTemperature(EnumTemperature.WARM, EnumTolerance.BOTH_1)
/* 271 */       .setStemColor(EnumFlowerColor.GREEN);
/*     */     
/* 273 */     YARROW.setTraits(EnumAllele.Fertility.HIGH, EnumAllele.Lifespan.NORMAL, EnumAllele.Sappiness.LOW)
/* 274 */       .setMutation(DANDELION, ORCHID, 10)
/* 275 */       .setPH(EnumAcidity.ACID, EnumTolerance.UP_1)
/* 276 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.DOWN_1)
/* 277 */       .setStemColor(EnumFlowerColor.DARK_OLIVE_GREEN);
/*     */     
/* 279 */     PETUNIA.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.SHORTER, EnumAllele.Sappiness.AVERAGE)
/* 280 */       .setMutation(TULIP, DAHLIA, 5)
/* 281 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.DOWN_1)
/* 282 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.UP_1)
/* 283 */       .setTemperature(EnumTemperature.WARM, EnumTolerance.UP_1)
/* 284 */       .setStemColor(EnumFlowerColor.GREEN);
/*     */     
/* 286 */     AGAPANTHUS.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.NORMAL, EnumAllele.Sappiness.LOW)
/* 287 */       .setMutation(ALLIUM, GERANIUM, 5)
/* 288 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.BOTH_1)
/* 289 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.DOWN_1)
/* 290 */       .setTemperature(EnumTemperature.WARM, EnumTolerance.BOTH_1)
/* 291 */       .setStemColor(EnumFlowerColor.DARK_OLIVE_GREEN);
/*     */     
/* 293 */     FUCHSIA.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.SHORTENED, EnumAllele.Sappiness.AVERAGE)
/* 294 */       .setMutation(FOXGLOVE, DAHLIA, 5)
/* 295 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.BOTH_1)
/* 296 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.UP_1)
/* 297 */       .setTemperature(EnumTemperature.WARM, EnumTolerance.BOTH_1)
/* 298 */       .setStemColor(EnumFlowerColor.SEA_GREEN);
/*     */     
/* 300 */     DIANTHUS.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.SHORT, EnumAllele.Sappiness.HIGH)
/* 301 */       .setMutation(TULIP, POPPY, 15)
/* 302 */       .setPH(EnumAcidity.ALKALINE, EnumTolerance.DOWN_1)
/* 303 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.DOWN_1)
/* 304 */       .setTemperature(EnumTemperature.NORMAL, EnumTolerance.BOTH_2)
/* 305 */       .setStemColor(EnumFlowerColor.OLIVE_DRAB);
/*     */     
/* 307 */     FORGET.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.SHORT, EnumAllele.Sappiness.LOWER)
/* 308 */       .setMutation(ORCHID, BLUET, 10)
/* 309 */       .setPH(EnumAcidity.ACID, EnumTolerance.NONE)
/* 310 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.UP_1)
/* 311 */       .setTemperature(EnumTemperature.NORMAL, EnumTolerance.UP_1)
/* 312 */       .setStemColor(EnumFlowerColor.GREEN);
/*     */     
/* 314 */     ANEMONE.setTraits(EnumAllele.Fertility.HIGH, EnumAllele.Lifespan.NORMAL, EnumAllele.Sappiness.LOW)
/* 315 */       .setMutation(AQUILEGIA, ROSE, 5)
/* 316 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.BOTH_1)
/* 317 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.UP_1)
/* 318 */       .setStemColor(EnumFlowerColor.DARK_OLIVE_GREEN);
/*     */     
/* 320 */     AQUILEGIA.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.NORMAL, EnumAllele.Sappiness.AVERAGE)
/* 321 */       .setMutation(IRIS, POPPY, 5)
/* 322 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.UP_1)
/* 323 */       .setStemColor(EnumFlowerColor.MEDIUM_SEA_GREEN);
/*     */     
/* 325 */     EDELWEISS.setTraits(EnumAllele.Fertility.LOW, EnumAllele.Lifespan.NORMAL, EnumAllele.Sappiness.LOWEST)
/* 326 */       .setMutation(PEONY, BLUET, 5)
/* 327 */       .setPH(EnumAcidity.ALKALINE, EnumTolerance.DOWN_1)
/* 328 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.DOWN_1)
/* 329 */       .setTemperature(EnumTemperature.NORMAL, EnumTolerance.DOWN_1)
/* 330 */       .setStemColor(EnumFlowerColor.DARK_OLIVE_GREEN);
/*     */     
/* 332 */     SCABIOUS.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.SHORTENED, EnumAllele.Sappiness.LOW)
/* 333 */       .setMutation(ALLIUM, CORNFLOWER, 5)
/* 334 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.UP_1)
/* 335 */       .setTemperature(EnumTemperature.NORMAL, EnumTolerance.DOWN_1)
/* 336 */       .setStemColor(EnumFlowerColor.OLIVE_DRAB);
/*     */     
/* 338 */     CONEFLOWER.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.NORMAL, EnumAllele.Sappiness.HIGHER)
/* 339 */       .setMutation(TULIP, CORNFLOWER, 5)
/* 340 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.BOTH_1)
/* 341 */       .setStemColor(EnumFlowerColor.DARK_OLIVE_GREEN);
/*     */     
/* 343 */     GAILLARDIA.setTraits(EnumAllele.Fertility.HIGH, EnumAllele.Lifespan.LONG, EnumAllele.Sappiness.HIGHER)
/* 344 */       .setMutation(DANDELION, MARIGOLD, 5)
/* 345 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.BOTH_1)
/* 346 */       .setMoisture(EnumMoisture.DAMP, EnumTolerance.DOWN_1)
/* 347 */       .setTemperature(EnumTemperature.NORMAL, EnumTolerance.BOTH_2)
/* 348 */       .setStemColor(EnumFlowerColor.OLIVE_DRAB);
/*     */     
/* 350 */     AURICULA.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.ELONGATED, EnumAllele.Sappiness.HIGH)
/* 351 */       .setMutation(POPPY, GERANIUM, 10)
/* 352 */       .setPH(EnumAcidity.ACID, EnumTolerance.UP_1)
/* 353 */       .setMoisture(EnumMoisture.NORMAL, EnumTolerance.UP_1)
/* 354 */       .setStemColor(EnumFlowerColor.DARK_OLIVE_GREEN);
/*     */     
/* 356 */     CAMELLIA.setTraits(EnumAllele.Fertility.NORMAL, EnumAllele.Lifespan.NORMAL, EnumAllele.Sappiness.HIGH)
/* 357 */       .setMutation(HYDRANGEA, ROSE, 5)
/* 358 */       .setPH(EnumAcidity.ACID, EnumTolerance.NONE)
/* 359 */       .setMoisture(EnumMoisture.DAMP, EnumTolerance.NONE)
/* 360 */       .setTemperature(EnumTemperature.WARM, EnumTolerance.UP_1)
/* 361 */       .setStemColor(EnumFlowerColor.DARK_OLIVE_GREEN);
/*     */     
/* 363 */     GOLDENROD.setTraits(EnumAllele.Fertility.HIGH, EnumAllele.Lifespan.NORMAL, EnumAllele.Sappiness.HIGHER)
/* 364 */       .setMutation(LILAC, MARIGOLD, 10)
/* 365 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.BOTH_1)
/* 366 */       .setStemColor(EnumFlowerColor.MEDIUM_SEA_GREEN);
/*     */     
/* 368 */     ALTHEA.setTraits(EnumAllele.Fertility.LOW, EnumAllele.Lifespan.ELONGATED, EnumAllele.Sappiness.HIGH)
/* 369 */       .setMutation(HYDRANGEA, IRIS, 5)
/* 370 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.DOWN_1)
/* 371 */       .setTemperature(EnumTemperature.WARM, EnumTolerance.BOTH_1)
/* 372 */       .setStemColor(EnumFlowerColor.DARK_GREEN);
/*     */     
/* 374 */     PENSTEMON.setTraits(EnumAllele.Fertility.LOW, EnumAllele.Lifespan.LONG, EnumAllele.Sappiness.LOW)
/* 375 */       .setMutation(PEONY, LILAC, 5)
/* 376 */       .setMoisture(EnumMoisture.DRY, EnumTolerance.UP_1)
/* 377 */       .setTemperature(EnumTemperature.WARM, EnumTolerance.UP_1)
/* 378 */       .setStemColor(EnumFlowerColor.OLIVE_DRAB);
/*     */     
/* 380 */     DELPHINIUM.setTraits(EnumAllele.Fertility.LOW, EnumAllele.Lifespan.LONGER, EnumAllele.Sappiness.LOW)
/* 381 */       .setMutation(LILAC, BLUET, 5)
/* 382 */       .setMoisture(EnumMoisture.DAMP, EnumTolerance.DOWN_1)
/* 383 */       .setTemperature(EnumTemperature.NORMAL, EnumTolerance.DOWN_1)
/* 384 */       .setStemColor(EnumFlowerColor.DARK_SEA_GREEN);
/*     */     
/* 386 */     HOLLYHOCK.setTraits(EnumAllele.Fertility.LOW, EnumAllele.Lifespan.LONG, EnumAllele.Sappiness.HIGH)
/* 387 */       .setMutation(DELPHINIUM, LAVENDER, 5)
/* 388 */       .setPH(EnumAcidity.NEUTRAL, EnumTolerance.UP_1)
/* 389 */       .setStemColor(EnumFlowerColor.GREEN);
/*     */     
/* 391 */     IFlowerRoot flowerRoot = BotanyCore.getFlowerRoot();
/* 392 */     flowerRoot.addConversion(new ItemStack((Block)Blocks.field_150327_N, 1, 0), DANDELION.getTemplate());
/* 393 */     flowerRoot.addConversion(new ItemStack((Block)Blocks.field_150328_O, 1, 0), POPPY.getTemplate());
/* 394 */     flowerRoot.addConversion(new ItemStack((Block)Blocks.field_150328_O, 1, 1), ORCHID.getTemplate());
/* 395 */     flowerRoot.addConversion(new ItemStack((Block)Blocks.field_150328_O, 1, 2), ALLIUM.getTemplate());
/* 396 */     flowerRoot.addConversion(new ItemStack((Block)Blocks.field_150328_O, 1, 3), BLUET.getTemplate());
/* 397 */     flowerRoot.addConversion(new ItemStack((Block)Blocks.field_150328_O, 1, 7), TULIP.getTemplate());
/* 398 */     flowerRoot.addConversion(new ItemStack((Block)Blocks.field_150328_O, 1, 8), DAISY.getTemplate());
/* 399 */     flowerRoot.addConversion(new ItemStack((Block)Blocks.field_150398_cm, 1, 1), LILAC.getTemplate());
/* 400 */     flowerRoot.addConversion(new ItemStack((Block)Blocks.field_150398_cm, 1, 4), ROSE.getTemplate());
/* 401 */     flowerRoot.addConversion(new ItemStack((Block)Blocks.field_150398_cm, 1, 5), PEONY.getTemplate());
/* 402 */     flowerRoot.addConversion(new ItemStack((Block)Blocks.field_150328_O, 1, 6), TULIP.addVariant(EnumFlowerColor.WHITE));
/* 403 */     flowerRoot.addConversion(new ItemStack((Block)Blocks.field_150328_O, 1, 4), TULIP.addVariant(EnumFlowerColor.CRIMSON));
/* 404 */     flowerRoot.addConversion(new ItemStack((Block)Blocks.field_150328_O, 1, 5), TULIP.addVariant(EnumFlowerColor.DARK_ORANGE));
/*     */     
/* 406 */     for (FlowerSpecies species : values()) {
/*     */       
/* 408 */       String scientific = species.branchName.substring(0, 1).toUpperCase() + species.branchName.substring(1).toLowerCase();
/* 409 */       String uid = "flowers." + species.branchName.toLowerCase();
/* 410 */       IClassification branch = AlleleManager.alleleRegistry.getClassification("genus." + uid);
/* 411 */       if (branch == null) {
/* 412 */         branch = AlleleManager.alleleRegistry.createAndRegisterClassification(IClassification.EnumClassLevel.GENUS, uid, scientific);
/*     */       }
/* 414 */       species.branch = branch;
/* 415 */       branch.addMemberSpecies((IAlleleSpecies)species);
/*     */     } 
/*     */   }
/*     */   
/*     */   private FlowerSpecies setStemColor(EnumFlowerColor green) {
/* 420 */     this.stemColor = green;
/* 421 */     return this;
/*     */   }
/*     */   
/*     */   private FlowerSpecies setTraits(EnumAllele.Fertility fertility, EnumAllele.Lifespan shortened, EnumAllele.Sappiness lower) {
/* 425 */     this.fert = fertility;
/* 426 */     this.life = shortened;
/* 427 */     this.sap = lower;
/* 428 */     return this;
/*     */   }
/*     */   
/*     */   private FlowerSpecies setMutation(FlowerSpecies dandelion2, FlowerSpecies tulip2, int chance) {
/* 432 */     BotanyCore.getFlowerRoot().registerMutation((IMutation)new FlowerMutation(dandelion2, tulip2, getTemplate(), chance));
/* 433 */     return this;
/*     */   }
/*     */   
/*     */   private IAllele[] addVariant(EnumFlowerColor a, EnumFlowerColor b) {
/* 437 */     IAllele[] template = getTemplate();
/* 438 */     template[EnumFlowerChromosome.PRIMARY.ordinal()] = (IAllele)a.getAllele();
/* 439 */     template[EnumFlowerChromosome.SECONDARY.ordinal()] = (IAllele)b.getAllele();
/* 440 */     this.variantTemplates.add(template);
/* 441 */     return template;
/*     */   }
/*     */   
/*     */   private FlowerSpecies setTemperature(EnumTemperature temperature, EnumTolerance tolerance) {
/* 445 */     this.temperature = temperature;
/* 446 */     this.tempTolerance = tolerance;
/* 447 */     return this;
/*     */   }
/*     */   
/*     */   private FlowerSpecies setPH(EnumAcidity temperature, EnumTolerance tolerance) {
/* 451 */     this.pH = temperature;
/* 452 */     this.pHTolerance = tolerance;
/* 453 */     return this;
/*     */   }
/*     */   
/*     */   private FlowerSpecies setMoisture(EnumMoisture temperature, EnumTolerance tolerance) {
/* 457 */     this.moisture = temperature;
/* 458 */     this.moistureTolerance = tolerance;
/* 459 */     return this;
/*     */   }
/*     */   
/*     */   private IAllele[] addVariant(EnumFlowerColor a) {
/* 463 */     return addVariant(a, a);
/*     */   }
/*     */   
/*     */   public List<IAllele[]> getVariants() {
/* 467 */     return this.variantTemplates;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 472 */     return I18N.localise("botany.flower." + this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 477 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumTemperature getTemperature() {
/* 482 */     return this.temperature;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumHumidity getHumidity() {
/* 487 */     return EnumHumidity.values()[getMoisture().ordinal()];
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumAcidity getPH() {
/* 492 */     return this.pH;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumMoisture getMoisture() {
/* 497 */     return this.moisture;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEffect() {
/* 502 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSecret() {
/* 507 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCounted() {
/* 512 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBinomial() {
/* 517 */     return this.binomial;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAuthority() {
/* 522 */     return "Binnie";
/*     */   }
/*     */ 
/*     */   
/*     */   public IClassification getBranch() {
/* 527 */     return this.branch;
/*     */   }
/*     */   
/*     */   public void setBranch(IClassification branch) {
/* 531 */     this.branch = branch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getUID() {
/* 542 */     return this.uid;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getId() {
/* 547 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDominant() {
/* 553 */     return false;
/*     */   }
/*     */   
/*     */   public IAllele[] getTemplate() {
/* 557 */     AlleleHelper alleleHelper = AlleleHelper.instance;
/* 558 */     IAllele[] template = FlowerTemplates.getDefaultTemplate();
/* 559 */     template[0] = (IAllele)this;
/* 560 */     alleleHelper.set(template, (Enum)EnumFlowerChromosome.PRIMARY, AlleleHelper.getAllele(this.primaryColor));
/* 561 */     alleleHelper.set(template, (Enum)EnumFlowerChromosome.SECONDARY, AlleleHelper.getAllele(this.secondaryColor));
/* 562 */     alleleHelper.set(template, (Enum)EnumFlowerChromosome.TEMPERATURE_TOLERANCE, AlleleHelper.getAllele(this.tempTolerance));
/* 563 */     alleleHelper.set(template, (Enum)EnumFlowerChromosome.PH_TOLERANCE, AlleleHelper.getAllele(this.pHTolerance));
/* 564 */     alleleHelper.set(template, (Enum)EnumFlowerChromosome.HUMIDITY_TOLERANCE, AlleleHelper.getAllele(this.moistureTolerance));
/* 565 */     alleleHelper.set(template, (Enum)EnumFlowerChromosome.FERTILITY, AlleleHelper.getAllele(this.fert));
/* 566 */     alleleHelper.set(template, (Enum)EnumFlowerChromosome.LIFESPAN, AlleleHelper.getAllele(this.life));
/* 567 */     alleleHelper.set(template, (Enum)EnumFlowerChromosome.SAPPINESS, AlleleHelper.getAllele(this.sap));
/* 568 */     alleleHelper.set(template, (Enum)EnumFlowerChromosome.STEM, AlleleHelper.getAllele(this.stemColor));
/* 569 */     return template;
/*     */   }
/*     */ 
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public IIconProvider getIconProvider() {
/* 575 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ISpeciesRoot getRoot() {
/* 580 */     return (ISpeciesRoot)BotanyCore.getFlowerRoot();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIconColour(int renderPass) {
/* 585 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getComplexity() {
/* 590 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getResearchSuitability(ItemStack itemstack) {
/* 595 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack[] getResearchBounty(World world, GameProfile researcher, IIndividual individual, int bountyLevel) {
/* 600 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public IFlowerType getType() {
/* 605 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUnlocalizedName() {
/* 610 */     return getUID();
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin_Naz\AppData\Roaming\.loliland\game-resources\clients\techno_magic_sky\main\mods\binnie-mods-1.7.10-2.0.22.7.jar!\binnie\botany\genetics\FlowerSpecies.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */