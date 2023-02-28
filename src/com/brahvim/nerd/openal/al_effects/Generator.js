const ARRAY = [
    "AL_INSERT",
    "AL_SOMETHING",
    "AL_FUNNY",
    "AL_HERE",
    ":)"
];

let output = "";

// #region Getters!
output += "// region Getters.\n";
for (const x of ARRAY) {
    if (Array.isArray(x))
        output += writeGetter(x[0], x[1]);
    else
        output += writeGetter(x);
    output += "\n";
}
output += "// endregion\n\n";
// #endregion

// #region Setters!
output += "// region Setters.\n";
for (const x of ARRAY) {
    if (Array.isArray(x))
        output += writeSetter(x[0], x[1]);
    else
        output += writeSetter(x);
    output += "\n";
}
output += "// endregion";
// #endregion

console.log(output);

function writeGetter(p_prop, p_type = "float") {
    let fxnName = "get" + upperSnakeToPascalCase(
        p_prop.substring(
            // Get the index of the second `_`:
            1 + p_prop.indexOf('_', p_prop.indexOf('_'))
        ));

    return `public ${p_type} ${fxnName}() {
        return super.get${capitalizeFirstChar(p_type)}(EXTEfx.${p_prop});
    }`;
}

function writeSetter(p_prop, p_type = "float") {
    let paramName = p_type.includes("[]") ? "... p_values" : " p_value";
    let fxnName = "set" + upperSnakeToPascalCase(
        p_prop.substring(
            // Get the index of the second `_`:
            1 + p_prop.indexOf('_', p_prop.indexOf('_'))
        ));

    return `public void ${fxnName}(${p_type}${paramName}) {
        super.set${capitalizeFirstChar(p_type)}(EXTEfx.${p_prop},${paramName});
    }`;
}

// #region Case conversion code.
function upperSnakeToPascalCase(p_str) {
    return capitalizeFirstChar(upperSnakeToCamelCase(p_str));
}

function upperSnakeToCamelCase(p_str) {
    p_str = p_str.toLowerCase();
    let build = '';

    const STR_LEN = p_str.length;
    let lastUn = 0, secLastUn = 0;

    for (let i = 0; i != STR_LEN; i++) {
        if (p_str.charAt(i) != '_')
            continue;

        secLastUn = lastUn;
        lastUn = i;

        if (secLastUn == 0)
            build += p_str.substring(0, i);
        else build += capitalizeFirstChar(p_str.substring(
            secLastUn + 1, lastUn
        ));
    }

    build += capitalizeFirstChar(p_str.substring(1 + p_str.lastIndexOf('_')));

    return build;
}

function capitalizeFirstChar(p_str) {
    return p_str.charAt(0).toUpperCase() + p_str.substring(1);
}
// #endregion
