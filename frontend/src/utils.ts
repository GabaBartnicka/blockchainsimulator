export const shorterHash = (hash) => {
    return hash.substr(0, 10) + '...' + hash.substr(-10);
}
